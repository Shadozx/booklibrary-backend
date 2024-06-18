package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.repository.book.BookSeriesRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookSeriesService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookSeriesServiceImpl implements BookSeriesService {

    private final BookService bookService;
    private final BookSeriesRepository bookSeriesRepository;

    @Autowired
    public BookSeriesServiceImpl(BookService bookService, BookSeriesRepository bookSeriesRepository) {
        this.bookService = bookService;
        this.bookSeriesRepository = bookSeriesRepository;
    }

    @Override
    @Transactional
    public BookSeries create(BookSeries bookSeries) {
        checkIsBookSeriesNull(bookSeries);

        return save(bookSeries);
    }

    @Override
    public BookSeries readById(Long id) {
        return bookSeriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такої серії книг не існує..."));
    }

    @Override
    public BookSeries readByTitle(String title) {
        return findByTitle(title)
                .orElseThrow(() -> new EntityNotFoundException("Такої серії книг не існує..."));
    }

    @Override
    public boolean existsById(Long id) {
        return bookSeriesRepository.existsById(id);
    }

    @Override
    public boolean existsByTitle(String title) {
        return bookSeriesRepository.existsByTitleIgnoreCase(title);
    }

    @Override
    public boolean existsByUrl(String url) {
        return bookSeriesRepository.existsByUploadedUrl(url);
    }

    @Override
    public List<BookSeries> getBookSeriesByBook(Long bookId) {
        return bookService.readById(bookId).getSeries();
    }

    @Override
    @Transactional
    public BookSeries update(BookSeries bookSeries) {
        checkIsBookSeriesNull(bookSeries);

        readById(bookSeries.getId());

        return save(bookSeries);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public List<BookSeries> getAll() {
        return bookSeriesRepository.findAll();
    }

    Optional<BookSeries> findByTitle(String title) {
        return bookSeriesRepository.findByTitleIgnoreCase(title);
    }

    @Transactional
    BookSeries save(BookSeries bookSeries) {
        checkIsBookSeriesNull(bookSeries);

        return bookSeriesRepository.save(bookSeries);
    }

    @Transactional
    void delete(BookSeries bookSeries) {
        checkIsBookSeriesNull(bookSeries);

        bookSeriesRepository.delete(bookSeries);
    }

    private void checkIsBookSeriesNull(BookSeries bookSeries) {
        if (bookSeries == null) {
            throw new NullPointerException("Серія книг не може бути пустою");
        }
    }
}