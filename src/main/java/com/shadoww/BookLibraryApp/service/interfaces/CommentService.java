package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.comment.BookComment;
import com.shadoww.BookLibraryApp.model.comment.ChapterComment;
import com.shadoww.BookLibraryApp.model.comment.Comment;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;


public interface CommentService extends CrudService<Comment, Long> {

    List<BookComment> getAllBookComments();

    List<ChapterComment> getAllChapterComments();

    List<BookComment> getBookComments(Book book);

    List<ChapterComment> getChapterComments(Chapter chapter);

    List<BookComment> getOwnerBookComments(Book book, Person owner);

    List<ChapterComment> getOwnerChapterComments(Chapter chapter, Person owner);
}
