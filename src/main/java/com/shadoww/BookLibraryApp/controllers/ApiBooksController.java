package com.shadoww.BookLibraryApp.controllers;


import com.shadoww.BookLibraryApp.dto.request.books.BookFilterRequest;
import com.shadoww.BookLibraryApp.dto.request.books.BookLinkRequest;
import com.shadoww.BookLibraryApp.dto.response.BookResponse;
import com.shadoww.BookLibraryApp.dto.request.books.BookRequest;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.util.formatters.Formatter;
import com.shadoww.BookLibraryApp.util.parser.parsers.ParserHelper;
import com.shadoww.BookLibraryApp.util.responsers.ResponseBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books")
public class ApiBooksController {

    private final Formatter formatter;

    private final BooksService booksService;


    @Autowired
    public ApiBooksController(Formatter formatter, BooksService booksService) {
        this.formatter = formatter;
        this.booksService = booksService;
    }


    /**
     * Get books
     */
    @GetMapping
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok(
                booksService.findBooks()
                        .stream().map(BookResponse::new)
                        .toList()
        );
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterBooks(
            @RequestBody BookFilterRequest request) {

        return ResponseEntity.ok(booksService.filterBooks(request)
                .stream()
                .map(BookResponse::new)
                .toList());
    }

    /**
     * Add a book by a book's url
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody BookLinkRequest request) {
        String bookUrl = request.getBookUrl();

        System.out.println("BookUrl: " + "\"" + bookUrl + "\"");

        try {
            URL url = new URL(bookUrl);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Передано було не коректне посилання на книгу");
        }

        if (booksService.exists(bookUrl)) return ResponseBook.exist();

        boolean isAdded = formatter.format(bookUrl);
        System.out.println("Book was added: " + isAdded);

        if (isAdded) {
            return ResponseBook.addSuccess();

        }

        System.out.println("Error!");
        return ResponseBook.errorServer();

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable int id) {

        Book book = booksService.readById(id);

        return ResponseEntity.ok(new BookResponse(book));

    }

    /**
     * Create a book by book's form
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<?> newBook(@RequestBody BookRequest form) {

        if (form.isEmpty()) return ResponseBook.noContent();

        if (form.isTitleEmpty()) return ResponseBook.noContent("Назва книжки немає бути пустою");

        if (booksService.existByTitle(form.getTitle().trim())) return ResponseBook.exist();


        Book book = new Book();
        book.setTitle(form.getTitle());

        if (!form.isDescriptionEmpty()) {
            book.setDescription(form.getDescription());
        }

        if (!form.isBookImageUrlEmpty()) {
            try {
                BookImage bookImage = (BookImage) ParserHelper.parseImage(form.getBookImage());

                book.setBookImage(bookImage);
                bookImage.setBook(book);

                booksService.saveBookImage(book, bookImage);
            } catch (IOException e) {
                System.out.println("Error message in adding book with message:" + e.getMessage());
                return ResponseBook.errorServer();
            }
        } else {
            booksService.create(book);
        }

        return ResponseBook.addSuccess();

    }

    /**
     * Update a book by book's id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable int bookId,
                                        @RequestBody BookRequest form) {

        if (form.isEmpty()) return ResponseBook.noContent();

        Book book = booksService.readById(bookId);


        if (!form.isTitleEmpty()) {
            book.setTitle(form.getTitle());
        }

        if (!form.isDescriptionEmpty()) {
            book.setDescription(form.getDescription());
        }

        if (!form.isBookImageUrlEmpty()) {

            try {

                BookImage parsedImage = (BookImage) ParserHelper.parseImage(form.getBookImage());

                BookImage bookImage = book.getBookImage();

                if (bookImage == null) bookImage = new BookImage();

                bookImage.setData(parsedImage.getData());

                System.out.println("BookImage parsing...");
                System.out.println(bookImage);
                book.setBookImage(bookImage);
                bookImage.setBook(book);

                booksService.saveBookImage(book, bookImage);
            } catch (IOException e) {
                System.out.println("Error message in adding book with message:" + e.getMessage());
                return ResponseBook.errorServer();
            }
        } else {
            booksService.create(book);
        }

        return ResponseBook.addSuccess();
    }

    /**
     * Delete a book by book's id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {

        booksService.deleteById(id);


        return ResponseBook.deleteSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{bookId}/reload")
    public ResponseEntity<?> reloadChapters(@PathVariable int bookId) {
        Book book = booksService.readById(bookId);

        formatter.parseChapters(book);
        return ResponseEntity.ok("Дана книжка була успішно перегружена");
    }
}
