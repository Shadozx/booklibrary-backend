package com.shadoww.BookLibraryApp.repository.comment;

import com.shadoww.BookLibraryApp.model.comment.Comment;
import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByOwner(Person owner);
}
