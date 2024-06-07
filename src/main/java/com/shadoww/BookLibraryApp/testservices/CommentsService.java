package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.comments.BookComment;
import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import com.shadoww.BookLibraryApp.models.comments.Comment;
import com.shadoww.BookLibraryApp.models.user.Person;

import java.util.List;

public interface CommentsService {

    List<Comment> findAllComments();

    List<BookComment> findAllBookComments();

    List<ChapterComment> findAllChapterComments();

    List<BookComment> findByBook(Book book);

    List<ChapterComment> findByChapter(Chapter chapter);

    List<BookComment> findByBookAndOwner(Book book, Person owner);

    List<ChapterComment> findByChapterAndOwner(Chapter chapter, Person owner);

    Comment readById(int id);

    Comment create(Comment comment);

    void deleteById(int id);

    Comment save(Comment comment);

    void delete(Comment comment);
}
