package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;


public interface BookCatalogService extends CrudService<BookCatalog, Long> {

    BookCatalog getByIdAndPerson(Long id, Person owner);

    List<BookCatalog> getByPerson(Person owner);
}
