package com.shadoww.BookLibraryApp.controller;


import com.shadoww.BookLibraryApp.dto.response.ChapterResponse;
import com.shadoww.BookLibraryApp.dto.request.ChapterRequest;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
import com.shadoww.BookLibraryApp.service.interfaces.ImageService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseBook;
import com.shadoww.BookLibraryApp.util.responsers.ResponseChapter;
import com.shadoww.BookLibraryApp.util.texformatters.TextFormatter;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElement;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

//@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books/{bookId}/chapters")
public class ApiChaptersController {

    private BookService bookService;
    private ChapterService chapterService;

    private ImageService imageService;

//    private BooksFormatter formatter;

    @Autowired
    public ApiChaptersController(BookService bookService,
                                 ChapterService chapterService,
                                 ImageService imageService) {
        this.bookService = bookService;
        this.chapterService = chapterService;
        this.imageService = imageService;
//        this.formatter = formatter;
    }

    @GetMapping
    public ResponseEntity<?> getChaptersByBook(@PathVariable long bookId) {
        Book book = bookService.readById(bookId);

        return ResponseEntity.ok(
                book
                        .getChapters()
                        .stream()
                        .map(this::changeTextToHtml)
                        .map(ChapterResponse::new)
                        .toList()
        );

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addChapter(@PathVariable long bookId,
                                        @RequestBody ChapterRequest form
    ) {
        System.out.println("Form: " + form);

        Book book = bookService.readById(bookId);

        if (form.isEmpty()) return ResponseChapter.noContent();

        Chapter newChapter = new Chapter();

        if (form.isTitleEmpty() || form.isTextEmpty() || form.isNumberOfPageEmpty())
            return ResponseChapter.noContent();


        TextElements newElements = TextFormatter.parse(form.getText());

        if (newElements.isEmpty()) return ResponseChapter.noContent();


        List<ChapterImage> images = new ArrayList<>();
        for (var element : newElements) {
            if (element.hasType(ElementType.Image) && element.hasAttribute("data-filename")) {

                ChapterImage image = new ChapterImage();
                image.setContentType("image/jpeg");
                String decodedImage = element.attr("data");

                if (decodedImage != null && !decodedImage.equals("")) {

                    element.deleteAttribute("data-filename");
                    element.deleteAttribute("data");

                    byte[] data = Base64.getDecoder().decode(decodedImage.getBytes());


                    image.setChapterImage(book);
                    image.setChapter(newChapter);
                    element.addAttribute("filename", image.getFilename());

                    image.setData(data);
                    images.add(image);
                }
            }
        }


        newChapter.setBook(book);
        newChapter.setTitle(form.getTitle());

        newChapter.setText(newElements.toPatternText());

        newChapter.setChapterNumber(form.getChapterNumber());


        int amount = book.getAmount();
        book.setAmount(amount + 1);


        imageService.createChapterImages(images);

        System.out.println("Title:" + newChapter.getTitle());
        System.out.println("Text:" + newChapter.getText());
        System.out.println("Number:" + newChapter.getChapterNumber());
        chapterService.create(newChapter);
        bookService.create(book);


        return ResponseChapter.addSuccess();
    }

    @GetMapping("/{chapterId}")
    public ResponseEntity<?> getChapter(@PathVariable long bookId,
                                        @PathVariable long chapterId) {

        bookService.readById(bookId);

        Chapter chapter = chapterService.readById(chapterId);


        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());


        return ResponseEntity.ok(new ChapterResponse(chapter));
    }

    @GetMapping("/number/{chapterNumber}")
    public ResponseEntity<?> getChapterByBookAndChapterNumber(@PathVariable long bookId,
                                                              @PathVariable int chapterNumber) {

        Book book = bookService.readById(bookId);


        Chapter chapter = chapterService.getChapterByBookAndNumber(book, chapterNumber);


        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());

        return ResponseEntity.ok(new ChapterResponse(chapter));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{chapterId}")
    public ResponseEntity<?> updateChapter(@PathVariable long bookId,
                                           @PathVariable long chapterId,
                                           @RequestBody ChapterRequest form) {

        bookService.readById(bookId);

        Chapter chapter = chapterService.readById(chapterId);


        if (form.isEmpty()) return ResponseChapter.noContent();

        if (form.isTitleEmpty() || form.isTextEmpty() || form.isNumberOfPageEmpty()) return ResponseChapter.noContent();


        Book book = chapter.getBook();

        if (book == null) {
            return ResponseBook.noFound();
        }


        TextElements newElements = TextFormatter.parse(form.getText());

        if (newElements.isEmpty()) return ResponseChapter.noContent();


        chapter.setTitle(form.getTitle());
        chapter.setChapterNumber(form.getChapterNumber());

        TextElements oldElements = TextFormatter.parsePatternText(chapter.getText());

        if (newElements.equals(oldElements)) {

            return new ResponseEntity<>("Нічого нового не було додано", HttpStatus.SEE_OTHER);
        }

        List<TextElement> newImages = newElements.stream().filter(e -> e.hasType(ElementType.Image)).toList();

        for (var element : oldElements) {
            if (element.hasType(ElementType.Image) && !newImages.contains(element)) {
                imageService.deleteByFilename(element.attr("filename"));
            }
        }
//
        List<ChapterImage> images = new ArrayList<>();
        for (var element : newElements) {
            if (element.hasType(ElementType.Image) && element.hasAttribute("data-filename")) {

                ChapterImage image = new ChapterImage();
                image.setContentType("image/jpeg");
                String decodedImage = element.attr("data");

                if (decodedImage != null && !decodedImage.equals("")) {

                    element.deleteAttribute("data-filename");
                    element.deleteAttribute("data");

                    byte[] data = Base64.getDecoder().decode(decodedImage.getBytes());


                    image.setChapterImage(book);

                    element.addAttribute("filename", image.getFilename());

                    image.setData(data);
                    images.add(image);

                }

            }
        }

        imageService.createChapterImages(images);


        chapter.setText(newElements.toPatternText());

        chapterService.create(chapter);


        return ResponseChapter.updateSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable long bookId,
                                           @PathVariable long chapterId) {

        bookService.readById(bookId);
        chapterService.readById(chapterId);


//        bookService.deleteChapter(chapter);

        System.out.println("Chapter with id " + chapterId + " was deleted!");

        return ResponseChapter.deleteSuccess();
    }

    private Chapter changeTextToHtml(Chapter chapter) {
        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());

        return chapter;
    }
}
