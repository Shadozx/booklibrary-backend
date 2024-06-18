package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.BookMark;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.repository.BookMarksRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookMarkService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookMarkServiceImpl implements BookMarkService {

    private final BookMarksRepository bookMarksRepository;


    @Autowired
    public BookMarkServiceImpl(BookMarksRepository bookMarksRepository) {
        this.bookMarksRepository = bookMarksRepository;
    }

    @Override
    @Transactional
    public BookMark create(BookMark bookMark) {

        checkIsBookMarkNull(bookMark);

        return save(bookMark);
    }

    @Override
    public BookMark readById(Long id) {

        return bookMarksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такої закладки книги не існує"));
    }

    @Override
    public BookMark getBookMark(Long chapter, int paragraph) {

        return bookMarksRepository.findBookMarkByChapter_IdAndParagraph(chapter, paragraph)
                .orElseThrow(() -> new EntityNotFoundException("Такої закладки книги не існує"));
    }

    @Override
    public BookMark getBookMarkByCatalogAndBook(Long catalogId, Long bookId) {
        return bookMarksRepository.findByCatalog_IdAndBook_Id(catalogId, bookId)
                .orElseThrow(() -> new EntityNotFoundException("Такої закладки не існує"));
    }

    @Override
    public BookMark getByBookAndOwner(Book book, Person owner) {
        return bookMarksRepository.findByBookAndOwner(book, owner).orElseThrow(() -> new EntityNotFoundException("Такої закладки не існує"));
    }

    @Override
    @Transactional
    public BookMark update(BookMark updatedMark) {
        checkIsBookMarkNull(updatedMark);

        readById(updatedMark.getId());

        return save(updatedMark);
    }

    @Override
    public boolean existsById(Long id) {
        return bookMarksRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteBookMark(Long chapter, int paragraph) {
        bookMarksRepository.deleteBookMarkByChapter_IdAndParagraph(chapter, paragraph);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        BookMark mark = readById(id);
        delete(mark);
    }

    @Override
    public List<BookMark> getAll() {
        return bookMarksRepository.findAll();
    }

    @Transactional
    BookMark save(BookMark bookMark) {

        checkIsBookMarkNull(bookMark);
        return bookMarksRepository.save(bookMark);
    }

    @Transactional
    void delete(BookMark bookMark) {

        checkIsBookMarkNull(bookMark);
        bookMarksRepository.delete(bookMark);
    }

    private void checkIsBookMarkNull(BookMark mark) {
        if (mark == null) {
            throw new NullPointerException("Закладка не може бути пустою");
        }
    }
}
