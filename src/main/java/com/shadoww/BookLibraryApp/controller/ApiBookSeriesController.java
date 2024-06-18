package com.shadoww.BookLibraryApp.controller;

import com.shadoww.BookLibraryApp.dto.response.BookSeriesResponse;
import com.shadoww.BookLibraryApp.service.interfaces.BookSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/bookseries")
public class ApiBookSeriesController {

    private final BookSeriesService bookSeriesService;

    @Autowired
    public ApiBookSeriesController(BookSeriesService bookSeriesService) {
        this.bookSeriesService = bookSeriesService;
    }


    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(bookSeriesService.getAll()
                .stream()
                .map(BookSeriesResponse::new)
                .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookSeries(@PathVariable long id) {
        return ResponseEntity.ok(
                new BookSeriesResponse(bookSeriesService.readById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookSeries(@PathVariable long id) {
        bookSeriesService.deleteById(id);

        return ResponseEntity.ok("Серія книг була видалена");
    }
}
