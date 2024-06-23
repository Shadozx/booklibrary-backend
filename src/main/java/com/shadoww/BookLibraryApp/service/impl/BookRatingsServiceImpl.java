package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookRating;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.repository.BookRatingsRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookRatingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookRatingsServiceImpl implements BookRatingService {

    private final BookRatingsRepository bookRatingsRepository;

    @Autowired
    public BookRatingsServiceImpl(BookRatingsRepository bookRatingsRepository) {
        this.bookRatingsRepository = bookRatingsRepository;
    }

    @Override
    @Transactional
    public BookRating create(BookRating rating) {
        checkIsRatingNull(rating);

        return save(rating);
    }

    @Override
    public BookRating readById(Long ratingId) {
        return bookRatingsRepository.findById(ratingId).orElseThrow(()->new EntityNotFoundException("Такої оцінки не існує"));
    }

    @Override
    public List<BookRating> getBookRatingsByBook(Book book) {
        return bookRatingsRepository.findByBook(book);
    }

    @Override
    public BookRating getBookRatingByOwnerAndBook(Person owner, Book book) {
        return bookRatingsRepository.findByOwnerAndBook(owner, book).orElseThrow(()->new EntityNotFoundException("Такої оцінки не існує"));
    }

    @Override
    @Transactional
    public BookRating update(BookRating rating) {
        if(rating == null) {
            throw new NullPointerException("Оцінка книги не може бути пустою");
        }

        BookRating old = readById(rating.getId());

        old.setRating(rating.getRating());

        return save(old);
    }

    @Override
    public boolean existsById(Long id) {
        return bookRatingsRepository.existsById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return bookRatingsRepository.count();
    }

    @Override
    public List<BookRating> getAll() {
        return bookRatingsRepository.findAll();
    }

    @Transactional
    BookRating save(BookRating rating) {
        return bookRatingsRepository.save(rating);
    }

    @Transactional
    void delete(BookRating rating) {
        checkIsRatingNull(rating);

        bookRatingsRepository.delete(rating);
    }

    private void checkIsRatingNull(BookRating rating) {
        if(rating == null) {
            throw new NullPointerException("Оцінка книги не може бути пустою");
        }
    }
}
