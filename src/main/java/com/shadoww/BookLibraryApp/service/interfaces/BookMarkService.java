package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookMark;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.CrudService;

public interface BookMarkService extends CrudService<BookMark, Long> {

    BookMark getBookMark(Long chapter, int paragraph);

    BookMark getBookMarkByCatalogAndBook(Long catalogId, Long bookId);

    BookMark getByBookAndOwner(Book book, Person owner) ;

    void deleteBookMark(Long chapter, int paragraph);
}
