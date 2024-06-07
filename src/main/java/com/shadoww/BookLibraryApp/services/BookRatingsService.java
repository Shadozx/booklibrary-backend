package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookRating;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.repositories.BookRatingsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookRatingsService {

    private final BookRatingsRepository bookRatingsRepository;

    public BookRatingsService(BookRatingsRepository bookRatingsRepository) {
        this.bookRatingsRepository = bookRatingsRepository;
    }

    public List<BookRating> findAll() {
        return bookRatingsRepository.findAll();
    }

    public List<BookRating> findByBook(Book book) {
        return bookRatingsRepository.findByBook(book);
    }

    public BookRating findByOwnerAndBook(Person owner, Book book) {
        return bookRatingsRepository.findByOwnerAndBook(owner, book).orElseThrow(()->new EntityNotFoundException("Такої оцінки не існує"));
    }

    public BookRating readById(int ratingId) {
        return bookRatingsRepository.findById(ratingId).orElseThrow(()->new EntityNotFoundException("Такої оцінки не існує"));
    }

    @Transactional
    public BookRating create(BookRating rating) {
        if(rating == null) {
            throw new NullPointerException("Оцінка книги не може бути пустою");
        }
        return save(rating);
    }

    @Transactional
    public BookRating update(BookRating rating) {
        if(rating == null) {
            throw new NullPointerException("Оцінка книги не може бути пустою");
        }

        BookRating old = readById(rating.getId());

        old.setRating(rating.getRating());

        return save(old);
    }

    @Transactional
    public BookRating save(BookRating rating) {
        return bookRatingsRepository.save(rating);
    }

    @Transactional
    public void deleteById(int id) {

        delete(readById(id));
    }


    @Transactional
    public void delete(BookRating rating) {
        if(rating == null) {
            throw new NullPointerException("Оцінка книги не може бути пустою");
        }

        bookRatingsRepository.delete(rating);
    }
}
