package com.shadoww.BookLibraryApp.repositories.comments;

import com.shadoww.BookLibraryApp.models.comments.Comment;
import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByOwner(Person owner);
}
