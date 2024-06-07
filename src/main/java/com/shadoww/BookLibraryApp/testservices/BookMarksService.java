package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.user.Person;

import java.util.Optional;

public interface BookMarksService {

    BookMark readById(int id);

    BookMark findBookMark(int chapter, int paragraph);

    Optional<BookMark> findByCatalogAndBook(int catalogId, int bookId);

    BookMark findByBookAndOwner(Book book, Person owner);

    BookMark create(BookMark bookMark);

    BookMark save(BookMark bookMark);

    void deleteBookMark(int chapter, int paragraph);

    BookMark update(BookMark updated, int id);

    BookMark update(BookMark updatedMark);

    void deleteById(int id);

    void delete(BookMark bookMark);
}
