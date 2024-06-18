package com.shadoww.BookLibraryApp.controller;


import com.shadoww.BookLibraryApp.dto.request.BookMarkRequest;
import com.shadoww.BookLibraryApp.dto.response.BookMarkResponse;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.BookMark;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.security.AuthUser;
import com.shadoww.BookLibraryApp.service.interfaces.BookCatalogService;
import com.shadoww.BookLibraryApp.service.interfaces.BookMarkService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
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
    private final BookCatalogService bookCatalogService;
    private final BookMarkService bookMarkService;
    private final BookService bookService;
    private final ChapterService chapterService;

    @Autowired
    public ApiBookmarksController(BookCatalogService bookCatalogService,
                                  BookService bookService,
                                  ChapterService chapterService,
                                  BookMarkService bookMarkService) {

        this.bookCatalogService = bookCatalogService;
        this.bookService = bookService;
        this.chapterService = chapterService;
        this.bookMarkService = bookMarkService;
    }

    @GetMapping
    public ResponseEntity<?> getBookMarksByCatalog(@PathVariable long catalogId) {
        BookCatalog catalog = bookCatalogService.readById(catalogId);

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
                                         @PathVariable long catalogId,
                                         @RequestBody BookMarkRequest request) {

        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        Person owner = authUser.getPerson();

        BookCatalog catalog = bookCatalogService.readById(catalogId);


        if (owner.getId() != catalog.getOwner().getId()) {
            return ResponseUser.noFound();
        }

        Book book = bookService.readById(request.getBookId());

        BookMark bookMark = null;
        try {
            bookMark = bookMarkService.getByBookAndOwner(book, owner);

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
                .body(new BookMarkResponse(bookMarkService.create(bookMark)));
    }

    //    @GetMapping
//    public ResponseEntity<?> getBookMarkByBookAndCatalog(Authentication authentication,
//                                                         @PathVariable int catalogId,
//                                                         @RequestBody BookMarkRequest request) {
//        int bookId = request.getBookId();
//
//        Person owner = (Person) authentication.getPrincipal();
//        Book book = bookService.readById(bookId);
//
//    }
    @GetMapping("/{markId}")
    public ResponseEntity<?> getBookMark(@PathVariable long catalogId,
                                         @PathVariable long markId) {

        bookCatalogService.readById(catalogId);

        BookMark mark = bookMarkService.readById(markId);


        return ResponseEntity.ok(new BookMarkResponse(mark));
    }

    @PutMapping("/{markId}")
    public ResponseEntity<?> updateBookMark(Authentication authentication,
                                            @PathVariable long catalogId,
                                            @PathVariable long markId,
                                            @RequestBody BookMarkRequest request) {

        System.out.println("Update bookmark!");

        System.out.printf("Chapter id = %d and paragraph - %d%n", request.getChapterId(), request.getParagraph());
        BookCatalog catalog = bookCatalogService.readById(catalogId);

        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        Person person = authUser.getPerson();

        if (catalog.getOwner().getId() != person.getId()) {
            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
        }

        BookMark bookMark = bookMarkService.readById(markId);

        Chapter chapter = chapterService.readById(request.getChapterId());

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

        return ResponseEntity.ok(new BookMarkResponse(bookMarkService.update(bookMark)));
    }


    @DeleteMapping("/{markId}")
    public ResponseEntity<?> deleteMark(Authentication authentication,
                                        @PathVariable long catalogId,
                                        @PathVariable long markId) {
        System.out.println("DELETE METHOD BOOKMARK");


        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        Person person = authUser.getPerson();

        BookCatalog catalog = bookCatalogService.readById(catalogId);

        if (catalog.getOwner().getId() != person.getId()) {
            throw new AccessDeniedException("Ви не має доступу до цього ресурсу");
        }

        bookMarkService.deleteById(markId);

        return ResponseBookMark.deleteSuccess();
    }
}
