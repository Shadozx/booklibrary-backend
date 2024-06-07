package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.comments.BookComment;
import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import com.shadoww.BookLibraryApp.models.comments.Comment;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.repositories.comments.BookCommentsRepository;
import com.shadoww.BookLibraryApp.repositories.comments.ChapterCommentsRepository;
import com.shadoww.BookLibraryApp.repositories.comments.CommentsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentsService {
    private final CommentsRepository commentsRepository;

    private final BookCommentsRepository bookCommentsRepository;

    private final ChapterCommentsRepository chapterCommentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository,
                           BookCommentsRepository bookCommentsRepository,
                           ChapterCommentsRepository chapterCommentsRepository) {

        this.commentsRepository = commentsRepository;
        this.bookCommentsRepository = bookCommentsRepository;
        this.chapterCommentsRepository = chapterCommentsRepository;
    }

    public List<Comment> findAllComments() {
        return commentsRepository.findAll();
    }

    public List<BookComment> findAllBookComments() {
        return bookCommentsRepository.findAll();
    }

    public List<ChapterComment> findAllChapterComments() {
        return chapterCommentsRepository.findAll();
    }

    public List<BookComment> finByBook(Book book) {
        return bookCommentsRepository.findByBook(book);
    }

    public List<ChapterComment> finByChapter(Chapter chapter) {
        return chapterCommentsRepository.findByChapter(chapter);
    }

    public List<BookComment> finByBookAndOwner(Book book, Person owner) {
        return bookCommentsRepository.findByBookAndOwner(book, owner);
    }

    public List<ChapterComment> finByChapterAndOwner(Chapter chapter, Person owner) {
        return chapterCommentsRepository.findByChapterAndOwner(chapter, owner);
    }


    public Comment readById(int id) {
        return commentsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Коментарія не було знайдено"));
    }

    @Transactional
    public Comment create(Comment comment) {

        if (comment == null) {
            throw new NullPointerException("Коментарій не може бути пустим");
        }

        return save(comment);
    }

    @Transactional
    public void deleteById(int id) {
        delete(readById(id));
    }

    @Transactional
    public Comment save(Comment comment) {

        if (comment == null) {
            throw new NullPointerException("Коментарій не може бути пустим");
        }


        return commentsRepository.save(comment);
    }

    @Transactional
    public void delete(Comment comment) {
        if (comment == null) {
            throw new NullPointerException("Коментарій не може бути пустим");
        }

        commentsRepository.delete(comment);
    }
}
