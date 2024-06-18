package com.shadoww.BookLibraryApp.controller;

import com.shadoww.BookLibraryApp.dto.response.AuthorResponse;
import com.shadoww.BookLibraryApp.service.interfaces.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/authors")
public class ApiAuthorsController {

    private final AuthorService authorService;

    @Autowired
    public ApiAuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<?> getAuthors() {
        return ResponseEntity.ok(authorService.getAll()
                .stream()
                .map(AuthorResponse::new)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable long id) {
        return ResponseEntity.ok(
                new AuthorResponse(authorService.readById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable long id) {
        authorService.deleteById(id);

        return ResponseEntity.ok("Автор був видалений");
    }

}
