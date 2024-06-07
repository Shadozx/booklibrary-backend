package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookRating;
import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRatingsRepository extends JpaRepository<BookRating, Integer> {

    List<BookRating> findByBook(Book book);

    Optional<BookRating> findByOwnerAndBook(Person owner, Book book);
}
