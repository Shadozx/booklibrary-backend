package com.shadoww.BookLibraryApp.controller;

import com.shadoww.BookLibraryApp.dto.request.AuthorRequest;
import com.shadoww.BookLibraryApp.dto.response.AuthorResponse;
import com.shadoww.BookLibraryApp.exception.ValueAlreadyExistsException;
import com.shadoww.BookLibraryApp.model.Author;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable long id, @RequestBody AuthorRequest request) {

        System.out.println(request);


        if (authorService.existsByName(request.getName())) {
            throw new ValueAlreadyExistsException("Автор з таким іменем вже існує!");
        }

        Author author = authorService.readById(id);

        if(request.getName() != null) {
            author.setName(request.getName());
        }
        if(request.getBiography() != null) {
            author.setBiography(request.getBiography());
        }

        return ResponseEntity.ok(new AuthorResponse(authorService.update(author)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAuthor(@PathVariable long id) {
        authorService.deleteById(id);

        return ResponseEntity.ok("Автор був видалений");
    }

}
