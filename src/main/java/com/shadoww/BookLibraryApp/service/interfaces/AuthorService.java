package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;

public interface AuthorService extends CrudService<Author, Long> {

    Author readByName(String name);


    boolean existsByName(String name);
    boolean existsByUrl(String url);

    List<Author> getBookAuthors(Long bookId);
}
