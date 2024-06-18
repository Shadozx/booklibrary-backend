package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.dto.request.book.BookFilterRequest;
import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.repository.book.BooksRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {
    private final BooksRepository booksRepository;

    @Autowired
    public BookServiceImpl(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Override
    @Transactional
    public Book create(Book book) {

        checkIsBookNull(book);

        return save(book);
    }

    @Override
    public Book readById(Long id) {
        return booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такої книги не існує"));
    }

    @Override
    public Book getByUrl(String url) {
        return booksRepository.findByUploadedUrl(url).orElseThrow(() -> new EntityNotFoundException("Книжки з такою силкою не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return booksRepository.existsById(id);
    }

    @Override
    public boolean existByTitle(String title) {
        return booksRepository.existsBookByTitle(title);
    }

    @Override
    public boolean existsByUrl(String uploadedUrl) {
        return booksRepository.existsBookByUploadedUrl(uploadedUrl);
    }


    @Override
    @Transactional
    public Book update(Book updatedBook) {

        checkIsBookNull(updatedBook);

        readById(updatedBook.getId());

        return save(updatedBook);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public List<Book> getAll() {
        return booksRepository.findAll();
    }

    @Override
    public List<Book> filterBooks(BookFilterRequest filterRequest) {
        return getAll().stream()
                .filter(book -> filterRequest.getSearchText() == null || book.getTitle().toLowerCase().contains(filterRequest.getSearchText()))
                .filter(book -> filterRequest.getFromAmount() == null || book.getChapters().size() >= filterRequest.getFromAmount())
                .filter(book -> filterRequest.getToAmount() == null || book.getChapters().size() <= filterRequest.getToAmount())
                .filter(book -> filterRequest.getFromYear() == null || book.getCreatedAt().getYear() >= filterRequest.getFromYear())
                .filter(book -> filterRequest.getToYear() == null || book.getCreatedAt().getYear() <= filterRequest.getToYear())
                .toList();
    }

    @Override
    public List<Book> getBookSeriesBooks(BookSeries bookSeries) {
        return booksRepository.findBySeries(bookSeries);
    }

    @Override
    public List<Book> getAuthorBooks(Author author) {
        return booksRepository.findByAuthors(author);
    }

    @Transactional
    Book save(Book book) {
        checkIsBookNull(book);
        return booksRepository.save(book);
    }

    @Transactional
    void delete(Book book) {
        checkIsBookNull(book);
        booksRepository.delete(book);
    }

    private void checkIsBookNull(Book book) {
        if (book == null) {
            throw new NullPointerException("Книжка не може бути пустою");
        }
    }
}
