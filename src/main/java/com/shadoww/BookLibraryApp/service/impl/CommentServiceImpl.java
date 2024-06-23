package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.comment.BookComment;
import com.shadoww.BookLibraryApp.model.comment.ChapterComment;
import com.shadoww.BookLibraryApp.model.comment.Comment;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.repository.comment.BookCommentsRepository;
import com.shadoww.BookLibraryApp.repository.comment.ChapterCommentsRepository;
import com.shadoww.BookLibraryApp.repository.comment.CommentsRepository;
import com.shadoww.BookLibraryApp.service.interfaces.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentsRepository commentsRepository;

    private final BookCommentsRepository bookCommentsRepository;

    private final ChapterCommentsRepository chapterCommentsRepository;

    @Autowired
    public CommentServiceImpl(CommentsRepository commentsRepository,
                              BookCommentsRepository bookCommentsRepository,
                              ChapterCommentsRepository chapterCommentsRepository) {

        this.commentsRepository = commentsRepository;
        this.bookCommentsRepository = bookCommentsRepository;
        this.chapterCommentsRepository = chapterCommentsRepository;
    }

    @Override
    @Transactional
    public Comment create(Comment comment) {

        checkIsCommentNull(comment);

        return save(comment);
    }

    @Override
    public Comment readById(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Коментарія не було знайдено"));
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {

        checkIsCommentNull(comment);

        readById(comment.getId());
        return save(comment);
    }

    @Override
    public boolean existsById(Long id) {
        return commentsRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return commentsRepository.count();
    }

    @Override
    public List<Comment> getAll() {
        return commentsRepository.findAll();
    }

    @Override
    public List<BookComment> getAllBookComments() {
        return bookCommentsRepository.findAll();
    }

    @Override
    public List<ChapterComment> getAllChapterComments() {
        return chapterCommentsRepository.findAll();
    }

    @Override
    public List<BookComment> getBookComments(Book book) {
        return bookCommentsRepository.findByBook(book);
    }

    @Override
    public List<ChapterComment> getChapterComments(Chapter chapter) {
        return chapterCommentsRepository.findByChapter(chapter);
    }

    @Override
    public List<BookComment> getOwnerBookComments(Book book, Person owner) {
        return bookCommentsRepository.findByBookAndOwner(book, owner);
    }

    @Override
    public List<ChapterComment> getOwnerChapterComments(Chapter chapter, Person owner) {
        return chapterCommentsRepository.findByChapterAndOwner(chapter, owner);
    }

    @Transactional
    Comment save(Comment comment) {

        checkIsCommentNull(comment);

        return commentsRepository.save(comment);
    }

    @Transactional
    void delete(Comment comment) {

        checkIsCommentNull(comment);

        commentsRepository.delete(comment);
    }

    private void checkIsCommentNull(Comment comment) {
        if (comment == null) {
            throw new NullPointerException("Коментарій не може бути пустим");
        }
    }
}
