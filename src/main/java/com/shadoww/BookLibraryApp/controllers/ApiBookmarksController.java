package com.shadoww.BookLibraryApp.controllers;


import com.shadoww.BookLibraryApp.dto.request.BookMarkRequest;
import com.shadoww.BookLibraryApp.dto.response.BookMarkResponse;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.services.BookCatalogsService;
import com.shadoww.BookLibraryApp.services.BookMarksService;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.ChaptersService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseBookMark;
import com.shadoww.BookLibraryApp.util.responsers.ResponseUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/catalogs/{catalogId}/bookmarks")
public class ApiBookmarksController {
    private final BookCatalogsService bookCatalogsService;
    private final BookMarksService bookMarksService;
    private final BooksService booksService;
    private final ChaptersService chaptersService;

    @Autowired
    public ApiBookmarksController(BookCatalogsService bookCatalogsService,
                                  BooksService booksService,
                                  ChaptersService chaptersService,
                                  BookMarksService bookMarksService) {
        this.bookCatalogsService = bookCatalogsService;
        this.booksService = booksService;
        this.chaptersService = chaptersService;
        this.bookMarksService = bookMarksService;

    }

    @GetMapping
    public ResponseEntity<?> getBookMarksByCatalog(@PathVariable int catalogId) {
        BookCatalog catalog = bookCatalogsService.readById(catalogId);

        return ResponseEntity
                .ok(
                        catalog
                                .getBookMarks()
                                .stream()
                                .map(BookMarkResponse::new)
                                .toList()
                );
    }

    @PostMapping
    public ResponseEntity<?> addBookMark(Authentication authentication,
                                         @PathVariable int catalogId,
                                         @RequestBody BookMarkRequest request) {

        Person owner = (Person) authentication.getPrincipal();

        BookCatalog catalog = bookCatalogsService.readById(catalogId);


        if (owner.getId() != catalog.getOwner().getId()) {
            return ResponseUser.noFound();
        }


        Book book = booksService.readById(request.getBookId());

        BookMark bookMark = null;
        try {
            bookMark = bookMarksService.findByBookAndOwner(book, owner);

        } catch (EntityNotFoundException e) {

            System.out.println("Перевіряємо чи є в користувача з цією книгою закладка");
        }

        if (bookMark == null) {
            bookMark = new BookMark();

        }
        bookMark.setCatalog(catalog);
        bookMark.setBook(book);
        bookMark.setOwner(owner);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BookMarkResponse(bookMarksService.create(bookMark)));
    }

    //    @GetMapping
//    public ResponseEntity<?> getBookMarkByBookAndCatalog(Authentication authentication,
//                                                         @PathVariable int catalogId,
//                                                         @RequestBody BookMarkRequest request) {
//        int bookId = request.getBookId();
//
//        Person owner = (Person) authentication.getPrincipal();
//        Book book = booksService.readById(bookId);
//
//    }
    @GetMapping("/{markId}")
    public ResponseEntity<?> getBookMark(@PathVariable int catalogId,
                                         @PathVariable int markId) {

        bookCatalogsService.readById(catalogId);

        BookMark mark = bookMarksService.readById(markId);


        return ResponseEntity.ok(new BookMarkResponse(mark));
    }

    @PutMapping("/{markId}")
    public ResponseEntity<?> updateBookMark(Authentication authentication,
                                            @PathVariable int catalogId,
                                            @PathVariable int markId,
                                            @RequestBody BookMarkRequest request) {

        System.out.println("Update bookmark!");

        System.out.printf("Chapter id = %d and paragraph - %d%n", request.getChapterId(), request.getParagraph());
        BookCatalog catalog = bookCatalogsService.readById(catalogId);

        Person person = (Person) authentication.getPrincipal();

        if (catalog.getOwner().getId() != person.getId()) {
            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
        }

        BookMark bookMark = bookMarksService.readById(markId);

        Chapter chapter = chaptersService.readById(request.getChapterId());

        int paragraph = request.getParagraph();

        bookMark.setChapter(chapter);
        // якщо хочемо зробити закладку на рівні глави
        if (bookMark.getParagraph() == paragraph) {
            System.out.println("Deleting bookMark");

            bookMark.setParagraph(0);

        }

        // якщо хочемо зробити закладку на рівні параграфу глави
        else {
            System.out.println("Updating bookMark");

            bookMark.setParagraph(paragraph);
        }

        return ResponseEntity.ok(new BookMarkResponse(bookMarksService.update(bookMark)));
    }


    @DeleteMapping("/{markId}")
    public ResponseEntity<?> deleteMark(Authentication authentication,
                                        @PathVariable int catalogId,
                                        @PathVariable int markId) {
        System.out.println("DELETE METHOD BOOKMARK");


        Person person = (Person) authentication.getPrincipal();

        BookCatalog catalog = bookCatalogsService.readById(catalogId);

        if (catalog.getOwner().getId() != person.getId()) {
            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
        }

        bookMarksService.deleteById(markId);

        return ResponseBookMark.deleteSuccess();
    }
}
