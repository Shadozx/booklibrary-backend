package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.dto.request.books.BookFilterRequest;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface BooksService {

    List<Book> findBooks();

    List<Book> filterBooks(BookFilterRequest filterRequest);

    Book readById(int id);

    Page<Book> findBooksByPage(int page);

    boolean existByTitle(String title);

    Page<Book> findByTitle(String bookName, int page);

    Optional<Book> findByUrl(String url);

    boolean exists(String uploadedUrl);

    Book create(Book book);

    void saveChapter(int bookId, Chapter newChapter);

    void saveBookImage(Book book, BookImage bookImage);

    void updateBookImage(Book book, BookImage bookImage);

    void saveBook(Book book, List<Chapter> chapters);

    void saveBook(Book book, BookImage bookImage, List<Chapter> chapters);

    Book update(Book updatedBook);

    void updateBookAndImage(Book updated, BookImage bookImage);

    void deleteById(int id);

    void deleteChapter(Chapter chapter);

    void deleteAll();
}
