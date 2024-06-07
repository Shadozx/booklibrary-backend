package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookRating;
import com.shadoww.BookLibraryApp.models.user.Person;

import java.util.List;

public interface BookRatingsService {

    List<BookRating> findAll();

    List<BookRating> findByBook(Book book);

    BookRating findByOwnerAndBook(Person owner, Book book);

    BookRating readById(int ratingId);

    BookRating create(BookRating rating);

    BookRating update(BookRating rating);

    BookRating save(BookRating rating);

    void deleteById(int id);

    void delete(BookRating rating);
}
