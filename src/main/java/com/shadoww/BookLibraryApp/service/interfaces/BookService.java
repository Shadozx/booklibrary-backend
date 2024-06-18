package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.dto.request.book.BookFilterRequest;
import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;


public interface BookService extends CrudService<Book, Long> {

    Book getByUrl(String url);

    boolean existByTitle(String title);

    boolean existsByUrl(String uploadedUrl);

    List<Book> filterBooks(BookFilterRequest filterRequest);

    List<Book> getBookSeriesBooks(BookSeries bookSeries);

    List<Book> getAuthorBooks(Author author);

}