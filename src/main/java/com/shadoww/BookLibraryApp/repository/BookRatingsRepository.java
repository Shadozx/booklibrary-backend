package com.shadoww.BookLibraryApp.repository;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookRating;
import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingsRepository extends JpaRepository<BookRating, Long> {

    List<BookRating> findByBook(Book book);

    Optional<BookRating> findByOwnerAndBook(Person owner, Book book);
}
