package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookRating;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;

public interface BookRatingService extends CrudService<BookRating, Long> {

    List<BookRating> getBookRatingsByBook(Book book);

    BookRating getBookRatingByOwnerAndBook(Person owner, Book book);
}