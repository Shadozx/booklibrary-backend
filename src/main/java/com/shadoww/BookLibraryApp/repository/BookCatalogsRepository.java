package com.shadoww.BookLibraryApp.repository;

import com.shadoww.BookLibraryApp.model.BookCatalog;

import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCatalogsRepository extends JpaRepository<BookCatalog, Long> {


    List<BookCatalog> findBookCatalogByOwner(Person owner);


    Optional<BookCatalog> findBookCatalogByIdAndOwner(long id, Person owner);
}
