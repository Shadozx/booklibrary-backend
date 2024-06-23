package com.shadoww.BookLibraryApp.service;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.service.interfaces.AuthorService;
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
public class AuthorServiceTests {

    AuthorService authorService;

    BookService bookService;

    @Autowired
    public AuthorServiceTests(AuthorService authorService, BookService bookService) {

        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Test
    @DisplayName("test work author with books")
    @Transactional
    public void testDeleteAuthorWithBooks() {

        initialize();

        System.out.println("Start test deleting author");
        showAuthorsAndBooks();

        long expectedBooksCount = bookService.count();
        System.out.println(authorService.getAll());

        List<Author> authors = authorService.getAll();

        for (var a : authors) {
            authorService.deleteById(a.getId());
        }

        long authorCount = authorService.count();
        assertEquals(0, authorCount, "Authors must be zero but not - " + authorCount);

        long actualBooksCount = bookService.count();
        assertEquals(expectedBooksCount, actualBooksCount, "Books must be 3 but not - " + actualBooksCount);


        showAuthorsAndBooks();

        System.out.println("End deleting author");
    }

    @Test
    @DisplayName("test working delete book with author")
    @Transactional
    public void testDeleteBookWithAuthor() {

        initialize();
        System.out.println("Start test deleting book");
        showAuthorsAndBooks();

        System.out.println(authorService.getAll());

        long expectedAuthorsCount = authorService.count();

        List<Book> books = bookService.getAll();

        long expectedBooksCount = books.size() - 1;

        bookService.deleteById(books.get(0).getId());

        long actualAuthorsCount = authorService.count();
        assertEquals(expectedAuthorsCount, actualAuthorsCount, String.format("Authors must be %s but not - %s", expectedAuthorsCount, actualAuthorsCount));

        long actualBooksCount = bookService.count();
        assertEquals(expectedBooksCount, actualBooksCount, String.format("Books must be %s but not - %s", expectedBooksCount, actualBooksCount));

        showAuthorsAndBooks();

        System.out.println("End test deleting book");
    }

    private void initialize() {
        List<Book> books = new ArrayList<>();

        String title = "title#";
        for (int i = 1; i <= 3; i++) {
            books.add(createTest(title + i));
        }

        Author author = new Author();
        author.setName("test author name");

        author.addAllBooks(books);

        for (var b : books) {
            b.addAuthor(author);
        }

        System.out.println("here");


        authorService.create(author);
        books.forEach(b -> bookService.create(b));
    }

    private Book createTest(String title) {
        Book book = new Book();

        book.setTitle(title);

        return book;
    }

    private void showAuthorsAndBooks() {
        System.out.println("Authors: " + authorService.getAll());
        System.out.println("Books: " + bookService.getAll());
    }
}
