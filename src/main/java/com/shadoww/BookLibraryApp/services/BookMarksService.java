package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.repositories.BookMarksRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookMarksService {

    private final BookMarksRepository bookMarksRepository;


    @Autowired
    public BookMarksService(BookMarksRepository bookMarksRepository) {
        this.bookMarksRepository = bookMarksRepository;
    }

    public BookMark readById(int id) {

        return bookMarksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такої закладки книги не існує"));
    }

    public BookMark findBookMark(int chapter, int paragraph) {

        return bookMarksRepository.findBookMarkByChapter_IdAndParagraph(chapter, paragraph)
                .orElseThrow(() -> new EntityNotFoundException("Такої закладки книги не існує"));
    }

    public Optional<BookMark> findByCatalogAndBook(int catalogId, int bookId) {
        return bookMarksRepository.findByCatalog_IdAndBook_Id(catalogId, bookId);
    }

    public BookMark findByBookAndOwner(Book book, Person owner) {
        return bookMarksRepository.findByBookAndOwner(book, owner).orElseThrow(()->new EntityNotFoundException("Такої закладки не існує"));
    }

    @Transactional
    public BookMark create(BookMark bookMark) {

        if (bookMark == null) {
            throw new NullPointerException("Закладки книги не може бути пустою");
        }

        return save(bookMark);
    }

    @Transactional
    public BookMark save(BookMark bookMark) {
        return bookMarksRepository.save(bookMark);
    }


    @Transactional
    public void deleteBookMark(int chapter, int paragraph) {
        bookMarksRepository.deleteBookMarkByChapter_IdAndParagraph(chapter, paragraph);
    }


    @Transactional
    public BookMark update(BookMark updated, int id) {

        BookMark forUpdate = readById(id);


        forUpdate.setOwner(updated.getOwner());
        forUpdate.setBook(updated.getBook());

        forUpdate.setChapter(updated.getChapter());
        forUpdate.setCatalog(updated.getCatalog());
        forUpdate.setParagraph(updated.getParagraph());

        return save(forUpdate);

    }

    @Transactional
    public BookMark update(BookMark updatedMark) {
        if (updatedMark == null) {
            throw new NullPointerException("Закладки книги не може бути пустою");
        }

        readById(updatedMark.getId());

        return save(updatedMark);
    }

    @Transactional
    public void deleteById(int id) {
        BookMark mark = readById(id);
        delete(mark);
    }


    @Transactional
    public void delete(BookMark bookMark) {
        bookMarksRepository.delete(bookMark);
    }
}
