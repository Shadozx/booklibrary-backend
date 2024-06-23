package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.repository.book.AuthorsRepository;
import com.shadoww.BookLibraryApp.service.interfaces.AuthorService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorsRepository authorsRepository;

    private final BookService bookService;

    @Autowired
    public AuthorServiceImpl(AuthorsRepository authorsRepository, BookService bookService) {
        this.authorsRepository = authorsRepository;
        this.bookService = bookService;
    }

    @Override
    @Transactional
    public Author create(Author author) {
        checkIsAuthorNull(author);

        return save(author);
    }

    @Override
    public Author readById(Long id) {
        return authorsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такого автора не існує..."));
    }

    @Override
    public Author readByName(String name) {
        return findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Такого автора не існує!"));
    }

    @Override
    public boolean existsById(Long id) {
        return authorsRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return authorsRepository.existsByName(name);
    }

    @Override
    public boolean existsByUrl(String url) {
        return authorsRepository.existsByUploadedUrl(url);
    }

    @Override
    public List<Author> getBookAuthors(Long bookId) {
        return bookService.readById(bookId).getAuthors();
    }

    @Override
    @Transactional
    public Author update(Author author) {
        checkIsAuthorNull(author);

        readById(author.getId());

        return save(author);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Author author = readById(id);

        List<Book> books = author.getBooks();

        for (var b : books) {
            b.getAuthors().remove(author);
        }

        delete(author);
    }

    @Override
    public long count() {
        return authorsRepository.count();
    }


    Optional<Author> findByName(String name) {
        return authorsRepository.findByName(name);
    }

    @Override
    public List<Author> getAll() {
        return authorsRepository.findAll();
    }

    @Transactional
    Author save(Author author) {
        checkIsAuthorNull(author);

        return authorsRepository.save(author);
    }

    @Transactional
    void delete(Author author) {
        checkIsAuthorNull(author);

        authorsRepository.delete(author);
    }

    private void checkIsAuthorNull(Author author) {
        if (author == null) {
            throw new NullPointerException("Автор не може бути пустим");
        }
    }
}
