package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.user.Person;

import java.util.List;

public interface BookCatalogsService {

    BookCatalog getByIdAndPerson(int id, Person owner);

    BookCatalog readById(int id);

    List<BookCatalog> findByPerson(Person owner);

    void create(BookCatalog bookCatalog);

    BookCatalog update(BookCatalog updatedCatalog);

    void deleteById(int id);

    void delete(BookCatalog bookCatalog);
}
