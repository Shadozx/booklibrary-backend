package com.shadoww.BookLibraryApp.controller.book;

import com.shadoww.BookLibraryApp.dto.response.BookResponse;
import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.service.interfaces.AuthorService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/authors/{authorId}/books")
public class ApiAuthorBooksController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public ApiAuthorBooksController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getAuthorBooks(@PathVariable long authorId) {
        Author author = authorService.readById(authorId);

        return ResponseEntity.ok(
                bookService.getAuthorBooks(author)
                        .stream()
                        .map(BookResponse::new)
                        .toList()
        );
    }
}
