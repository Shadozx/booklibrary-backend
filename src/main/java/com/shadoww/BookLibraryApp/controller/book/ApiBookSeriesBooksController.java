package com.shadoww.BookLibraryApp.controller.book;

import com.shadoww.BookLibraryApp.dto.response.BookResponse;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.service.interfaces.BookSeriesService;
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
@RequestMapping("/api/bookseries/{seriesId}/books")
public class ApiBookSeriesBooksController {

    private final BookSeriesService bookSeriesService;

    private final BookService bookService;

    @Autowired
    public ApiBookSeriesBooksController(BookSeriesService bookSeriesService, BookService bookService) {
        this.bookSeriesService = bookSeriesService;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<?> getBookSeriesBooks(@PathVariable long seriesId) {
        BookSeries bookSeries = bookSeriesService.readById(seriesId);

        return ResponseEntity.ok(
                bookService.getBookSeriesBooks(bookSeries)
                        .stream()
                        .map(BookResponse::new)
                        .toList()
        );
    }
}
