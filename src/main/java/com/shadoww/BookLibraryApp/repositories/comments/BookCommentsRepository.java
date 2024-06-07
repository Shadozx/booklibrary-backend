package com.shadoww.BookLibraryApp.repositories.comments;


import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.comments.BookComment;
import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCommentsRepository extends JpaRepository<BookComment, Integer> {

    List<BookComment> findByBook(Book book);

    List<BookComment> findByBookAndOwner(Book book, Person owner);
}
