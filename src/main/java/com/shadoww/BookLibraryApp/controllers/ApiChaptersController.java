package com.shadoww.BookLibraryApp.controllers;


import com.shadoww.BookLibraryApp.dto.response.ChapterResponse;
import com.shadoww.BookLibraryApp.dto.request.ChapterRequest;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.ChaptersService;
import com.shadoww.BookLibraryApp.services.ImagesService;
import com.shadoww.BookLibraryApp.util.formatters.Formatter;
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
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books/{bookId}/chapters")
public class ApiChaptersController {

    private BooksService booksService;
    private ChaptersService chaptersService;

    private ImagesService imagesService;

//    private Formatter formatter;

    @Autowired
    public ApiChaptersController(BooksService booksService,
                                 ChaptersService chaptersService,
                                 ImagesService imagesService) {
        this.booksService = booksService;
        this.chaptersService = chaptersService;
        this.imagesService = imagesService;
//        this.formatter = formatter;
    }

    @GetMapping
    public ResponseEntity<?> getChaptersByBook(@PathVariable int bookId) {
        Book book = booksService.readById(bookId);

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
    public ResponseEntity<?> addChapter(@PathVariable int bookId,
                                        @RequestBody ChapterRequest form
    ) {
        System.out.println("Form: " + form);

        Book book = booksService.readById(bookId);

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

        newChapter.setNumberOfPage(form.getNumberOfPage());


        int amount = book.getAmount();
        book.setAmount(amount + 1);


        imagesService.saveChapterImages(images);

        System.out.println("Title:" + newChapter.getTitle());
        System.out.println("Text:" + newChapter.getText());
        System.out.println("Number:" + newChapter.getNumberOfPage());
        chaptersService.save(newChapter);
        booksService.create(book);


        return ResponseChapter.addSuccess();
    }


    @GetMapping("/{chapterId}")
    public ResponseEntity<?> getChapter(@PathVariable int bookId,
                                        @PathVariable int chapterId) {

        booksService.readById(bookId);

        Chapter chapter = chaptersService.readById(chapterId);


        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());


        return ResponseEntity.ok(new ChapterResponse(chapter));
    }

    @GetMapping("/number/{chapterNumber}")
    public ResponseEntity<?> getChapterByBookAndChapterNumber(@PathVariable int bookId,
                                                              @PathVariable int chapterNumber) {

        Book book = booksService.readById(bookId);


        Chapter chapter = chaptersService.findByBookAndNumber(book, chapterNumber);


        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());

        return ResponseEntity.ok(new ChapterResponse(chapter));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{chapterId}")
    public ResponseEntity<?> updateChapter(@PathVariable int bookId,
                                           @PathVariable int chapterId,
                                           @RequestBody ChapterRequest form) {

        booksService.readById(bookId);

        Chapter chapter = chaptersService.readById(chapterId);


        if (form.isEmpty()) return ResponseChapter.noContent();

        if (form.isTitleEmpty() || form.isTextEmpty() || form.isNumberOfPageEmpty()) return ResponseChapter.noContent();


        Book book = chapter.getBook();

        if (book == null) {
            return ResponseBook.noFound();
        }


        TextElements newElements = TextFormatter.parse(form.getText());

        if (newElements.isEmpty()) return ResponseChapter.noContent();


        chapter.setTitle(form.getTitle());
        chapter.setNumberOfPage(form.getNumberOfPage());

        TextElements oldElements = TextFormatter.parsePatternText(chapter.getText());

        if (newElements.equals(oldElements)) {

            return new ResponseEntity<>("Нічого нового не було додано", HttpStatus.SEE_OTHER);
        }

        List<TextElement> newImages = newElements.stream().filter(e -> e.hasType(ElementType.Image)).toList();

        for (var element : oldElements) {
            if (element.hasType(ElementType.Image) && !newImages.contains(element)) {
                imagesService.deleteByFilename(element.attr("filename"));
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

        imagesService.saveChapterImages(images);


        chapter.setText(newElements.toPatternText());

        chaptersService.save(chapter);


        return ResponseChapter.updateSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{chapterId}")
    public ResponseEntity<?> deleteChapter(@PathVariable int bookId,
                                           @PathVariable int chapterId) {

        Book book = booksService.readById(bookId);
        Chapter chapter = chaptersService.readById(chapterId);


        booksService.deleteChapter(chapter);

        System.out.println("Chapter with id " + chapterId + " was deleted!");

        return ResponseChapter.deleteSuccess();
    }

    private Chapter changeTextToHtml(Chapter chapter) {
        chapter.setText(TextFormatter.parsePatterns(chapter.getText()).html());

        return chapter;
    }
}
