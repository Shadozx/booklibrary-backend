package com.shadoww.BookLibraryApp.controllers;

import com.shadoww.BookLibraryApp.dto.request.books.BookFilterRequest;
import com.shadoww.BookLibraryApp.dto.request.books.BookRequest;
import com.shadoww.BookLibraryApp.dto.response.BookResponse;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.util.formatters.Formatter;
import com.shadoww.BookLibraryApp.util.responsers.ResponseBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ApiBooksControllerTest {

    @Mock
    private Formatter formatter;

    @Mock
    private BooksService booksService;

    @InjectMocks
    private ApiBooksController apiBooksController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBooks() {

        System.out.println("test");
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("Book 1");

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Book 2");

        when(booksService.findBooks()).thenReturn(Arrays.asList(book1, book2));

        ResponseEntity<?> response = apiBooksController.getBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookResponse> books = (List<BookResponse>) response.getBody();
        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    void testFilterBooks() {
        BookFilterRequest filterRequest = new BookFilterRequest();
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("Book 1");

        when(booksService.filterBooks(filterRequest)).thenReturn(Collections.singletonList(book1));

        ResponseEntity<?> response = apiBooksController.filterBooks(filterRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<BookResponse> books = (List<BookResponse>) response.getBody();
        assertNotNull(books);
        assertEquals(1, books.size());
    }

    @Test
    void testGetBook() {
        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Test Book");

        when(booksService.readById(bookId)).thenReturn(book);

        ResponseEntity<?> response = apiBooksController.getBook(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        BookResponse bookResponse = (BookResponse) response.getBody();
        assertNotNull(bookResponse);
        assertEquals(bookId, bookResponse.getId());
        assertEquals("Test Book", bookResponse.getTitle());
    }

    @Test
    void testNewBook() {
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("New Book");

        when(booksService.existByTitle("New Book")).thenReturn(false);

        ResponseEntity<?> response = apiBooksController.newBook(bookRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseBook.addSuccess().getBody(), response.getBody());
    }

    @Test
    void testNewBookWithEmptyTitle() {
        BookRequest bookRequest = new BookRequest();

        ResponseEntity<?> response = apiBooksController.newBook(bookRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ResponseBook.noContent().getBody(), response.getBody());
    }

    @Test
    void testUpdateBook() {
        int bookId = 1;
        BookRequest bookRequest = new BookRequest();
        bookRequest.setTitle("Updated Book");

        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Old Book");

        when(booksService.readById(bookId)).thenReturn(book);

        ResponseEntity<?> response = apiBooksController.updateBook(bookId, bookRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseBook.addSuccess().getBody(), response.getBody());
        assertEquals("Updated Book", book.getTitle());
    }

    @Test
    void testDeleteBook() {
        int bookId = 1;

        ResponseEntity<?> response = apiBooksController.deleteBook(bookId);

        verify(booksService, times(1)).deleteById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseBook.deleteSuccess().getBody(), response.getBody());
    }

    @Test
    void testReloadChapters() {
        int bookId = 1;
        Book book = new Book();
        book.setId(bookId);

        when(booksService.readById(bookId)).thenReturn(book);

        ResponseEntity<?> response = apiBooksController.reloadChapters(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Дана книжка була успішно перегружена", response.getBody());
    }
}
