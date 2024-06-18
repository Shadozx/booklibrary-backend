package com.shadoww.BookLibraryApp.repository.comment;


import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.comment.BookComment;
import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCommentsRepository extends JpaRepository<BookComment, Long> {

    List<BookComment> findByBook(Book book);

    List<BookComment> findByBookAndOwner(Book book, Person owner);
}
