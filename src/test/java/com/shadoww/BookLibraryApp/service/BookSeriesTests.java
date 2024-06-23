package com.shadoww.BookLibraryApp.service;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.service.interfaces.BookSeriesService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
public class BookSeriesTests {

    BookSeriesService bookSeriesService;

    BookService bookService;

    @Autowired
    public BookSeriesTests(BookSeriesService bookSeriesService,
                           BookService bookService) {
        this.bookSeriesService = bookSeriesService;
        this.bookService = bookService;
    }

    @Test
    @DisplayName("test work book-series with books")
    @Transactional
    public void testDeleteBookSeriesWithBooks() {

        initialize();

        System.out.println("Start test deleting bookseries");
        showBookSeriesAndBooks();

        long expectedBooksCount = bookService.count();
        System.out.println(bookSeriesService.getAll());

        List<BookSeries> bookSeries = bookSeriesService.getAll();

        for (var b : bookSeries) {
            bookSeriesService.deleteById(b.getId());
        }

        long actualBookSeriesCount = bookSeriesService.count();
        assertEquals(0, actualBookSeriesCount, "BookSeries must be zero but not - " + actualBookSeriesCount);

        long actualBooksCount = bookService.count();
        assertEquals(expectedBooksCount, actualBooksCount, "Books must be 3 but not - " + actualBooksCount);

        showBookSeriesAndBooks();

        System.out.println("End deleting author");
    }

    @Test
    @DisplayName("test working delete book with book-series")
    @Transactional
    public void testDeleteBookWithBookSeries() {

        initialize();
        System.out.println("Start test deleting book");
        showBookSeriesAndBooks();

        System.out.println(bookSeriesService.getAll());

        long expectedBookSeriesCount = bookSeriesService.count();

        List<Book> books = bookService.getAll();

        long expectedBooksCount = books.size() - 1;

        bookService.deleteById(books.get(0).getId());

        long actualBookSeriesCount = bookSeriesService.count();
        assertEquals(expectedBookSeriesCount, actualBookSeriesCount, String.format("BookSeries must be %s but not - %s", expectedBookSeriesCount, actualBookSeriesCount));

        long actualBooksCount = bookService.count();
        assertEquals(expectedBooksCount, actualBooksCount, String.format("Books must be %s but not - %s", expectedBooksCount, actualBooksCount));

        showBookSeriesAndBooks();

        System.out.println("End test deleting book");
    }

    private void initialize() {
        List<Book> books = new ArrayList<>();

        String title = "title#";
        for (int i = 1; i <= 3; i++) {
            books.add(createTest(title + i));
        }

        BookSeries bookSeries = new BookSeries();
        bookSeries.setTitle("test bookseries title");

        bookSeries.addAllBooks(books);

        for (var b : books) {
            b.addBookSeries(bookSeries);
        }

        System.out.println("here");


        bookSeriesService.create(bookSeries);
        books.forEach(b -> bookService.create(b));
    }

    private Book createTest(String title) {
        Book book = new Book();

        book.setTitle(title);

        return book;
    }

    private void showBookSeriesAndBooks() {
        System.out.println("BookSeries: " + bookSeriesService.getAll());
        System.out.println("Books: " + bookService.getAll());
    }
}