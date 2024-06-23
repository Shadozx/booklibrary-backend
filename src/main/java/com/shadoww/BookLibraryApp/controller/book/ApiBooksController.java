package com.shadoww.BookLibraryApp.controller.book;


import com.shadoww.BookLibraryApp.dto.request.book.BookFilterRequest;
import com.shadoww.BookLibraryApp.dto.request.book.BookLinkRequest;
import com.shadoww.BookLibraryApp.dto.request.book.BookRequest;
import com.shadoww.BookLibraryApp.dto.response.AuthorResponse;
import com.shadoww.BookLibraryApp.dto.response.BookResponse;
import com.shadoww.BookLibraryApp.dto.response.BookSeriesResponse;
import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.model.image.BookImage;
import com.shadoww.BookLibraryApp.service.interfaces.AuthorService;
import com.shadoww.BookLibraryApp.service.interfaces.BookSeriesService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.util.formatters.BooksFormatter;
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
import java.util.List;
import java.util.Map;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/books")
public class ApiBooksController {

    private final BooksFormatter booksFormatter;
    private final AuthorService authorService;
    private final BookSeriesService bookSeriesService;
    private final BookService bookService;

    @Autowired
    public ApiBooksController(BooksFormatter booksFormatter,
                              AuthorService authorService,
                              BookSeriesService bookSeriesService,
                              BookService bookService) {
        this.booksFormatter = booksFormatter;
        this.authorService = authorService;
        this.bookSeriesService = bookSeriesService;
        this.bookService = bookService;
    }


    /**
     * Get books
     */
    @GetMapping
    public ResponseEntity<?> getBooks() {
        return ResponseEntity.ok(
                bookService.getAll()
                        .stream().map(BookResponse::new)
                        .toList()
        );
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterBooks(@RequestBody BookFilterRequest request) {

        return ResponseEntity.ok(bookService.filterBooks(request)
                .stream()
                .map(BookResponse::new)
                .toList());
    }

    /**
     * Add a book by a book's url
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> parseBook(@RequestBody BookLinkRequest request) {
        String url = request.getUrl();

        System.out.println("BookUrl: " + "\"" + url + "\"");

        try {
            new URL(url);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Передано було не коректне посилання");
        }

        if (bookService.existsByUrl(url)) {
            return ResponseBook.exist();
        }

        boolean isAdded = booksFormatter.format(url);
        System.out.println("Book was added: " + isAdded);

        if (isAdded) {
            return ResponseEntity.ok("Посилання було успішно опрацьовано");
        }

        System.out.println("Error!");
        return ResponseBook.errorServer();

    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable long id) {

        Book book = bookService.readById(id);

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

        if (bookService.existByTitle(form.getTitle().trim())) return ResponseBook.exist();


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

//                bookService.saveBookImage(book, bookImage);
            } catch (IOException e) {
                System.out.println("Error message in adding book with message:" + e.getMessage());
                return ResponseBook.errorServer();
            }
        } else {
            bookService.create(book);
        }

        return ResponseBook.addSuccess();

    }

    /**
     * Update a book by book's id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable long bookId,
                                        @RequestBody BookRequest request) {

        if (request.isEmpty()) return ResponseBook.noContent();

        Book book = bookService.readById(bookId);


        if (!request.isTitleEmpty()) {
            book.setTitle(request.getTitle());
        }

        if (!request.isDescriptionEmpty()) {
            book.setDescription(request.getDescription());
        }

        if (!request.isBookImageUrlEmpty()) {

            try {

                BookImage parsedImage = (BookImage) ParserHelper.parseImage(request.getBookImage());

                BookImage bookImage = book.getBookImage();

                if (bookImage == null) bookImage = new BookImage();

                bookImage.setData(parsedImage.getData());

                System.out.println("BookImage parsing...");
                System.out.println(bookImage);
                book.setBookImage(bookImage);
                bookImage.setBook(book);

//                bookService.saveBookImage(book, bookImage);
            } catch (IOException e) {
                System.out.println("Error message in adding book with message:" + e.getMessage());
                return ResponseBook.errorServer();
            }
        }
        else {
            bookService.update(book);
        }

        return ResponseBook.addSuccess();
    }

    /**
     * Delete a book by book's id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable long id) {

        bookService.deleteById(id);

        return ResponseBook.deleteSuccess();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{bookId}/reload")
    public ResponseEntity<?> reloadChapters(@PathVariable long bookId) throws IOException {
        Book book = bookService.readById(bookId);

        booksFormatter.reParseBookChapters(book);

        return ResponseEntity.ok("Дана книжка була успішно перегружена");
    }

    @GetMapping("/{bookId}/authors")
    public ResponseEntity<?> getBookAuthors(@PathVariable long bookId) {
        List<AuthorResponse> authors = authorService.getBookAuthors(bookId)
                .stream()
                .map(AuthorResponse::new)
                .toList();

        return ResponseEntity.ok(authors);
    }

    // add book author
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/authors")
    public ResponseEntity<Void> addAuthor(@PathVariable long id,
                                          @RequestBody Map<String, Long> requestBody) {

        long authorId = requestBody.get("author_id");

        Book book = bookService.readById(id);

        Author author = authorService.readById(authorId);

        List<Author> authors = book.getAuthors();

        if (authors.contains(author)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        authors.add(author);

        bookService.update(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // add book author
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/authors")
    public ResponseEntity<Void> removeAuthor(@PathVariable long id,
                                          @RequestBody Map<String, Long> requestBody) {

        long authorId = requestBody.get("author_id");

        Book book = bookService.readById(id);

        Author author = authorService.readById(authorId);

        List<Author> authors = book.getAuthors();

        if (!authors.contains(author)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        authors.remove(author);

        bookService.update(book);

        return ResponseEntity
                .ok()
                .build();
    }

    @GetMapping("/{bookId}/bookseries")
    public ResponseEntity<?> getBookSeriesByBook(@PathVariable long bookId) {
        List<BookSeriesResponse> bookSeries = bookSeriesService.getBookSeriesByBook(bookId)
                .stream()
                .map(BookSeriesResponse::new)
                .toList();

        return ResponseEntity.ok(bookSeries);
    }

    // add a bookseries to book
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/bookseries")
    public ResponseEntity<Void> addBookSeries(@PathVariable long id,
                                          @RequestBody Map<String, Long> requestBody) {

        long bookSeriesId = requestBody.get("bookseries_id");

        Book book = bookService.readById(id);

        BookSeries bookSeries = bookSeriesService.readById(bookSeriesId);

        List<BookSeries> series = book.getSeries();

        if (series.contains(bookSeries)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        series.add(bookSeries);

        bookService.update(book);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    // remove a bookseries from book
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}/bookseries")
    public ResponseEntity<Void> removeBookSeries(@PathVariable long id,
                                             @RequestBody Map<String, Long> requestBody) {

        long bookSeriesId = requestBody.get("bookseries_id");

        Book book = bookService.readById(id);

        BookSeries bookSeries = bookSeriesService.readById(bookSeriesId);

        List<BookSeries> series = book.getSeries();

        if (!series.contains(bookSeries)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        series.add(bookSeries);

        bookService.update(book);

        return ResponseEntity
                .ok()
                .build();
    }
}
