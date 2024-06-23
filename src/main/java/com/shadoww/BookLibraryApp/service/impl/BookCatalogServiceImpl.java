package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.repository.BookCatalogsRepository;
import com.shadoww.BookLibraryApp.service.interfaces.BookCatalogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookCatalogServiceImpl implements BookCatalogService {

    private final BookCatalogsRepository bookCatalogsRepository;

    @Autowired
    public BookCatalogServiceImpl(BookCatalogsRepository bookCatalogsRepository) {
        this.bookCatalogsRepository = bookCatalogsRepository;
    }

    @Override
    @Transactional
    public BookCatalog create(BookCatalog bookCatalog) {

        checkIsCatalogNull(bookCatalog);

        return save(bookCatalog);
    }

    @Override
    public BookCatalog readById(Long id) {
        return bookCatalogsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return bookCatalogsRepository.existsById(id);
    }

    @Override
    public BookCatalog getByIdAndPerson(Long id, Person owner) {
        return bookCatalogsRepository
                .findBookCatalogByIdAndOwner(id, owner)
                .orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }

    @Override
    public List<BookCatalog> getByPerson(Person owner) {
        return bookCatalogsRepository.findBookCatalogByOwner(owner);
    }

    @Override
    @Transactional
    public BookCatalog update(BookCatalog updatedCatalog) {

        checkIsCatalogNull(updatedCatalog);

        readById(updatedCatalog.getId());

        return save(updatedCatalog);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        delete(readById(id));
    }

    @Override
    public long count() {
        return bookCatalogsRepository.count();
    }

    @Override
    public List<BookCatalog> getAll() {
        return bookCatalogsRepository.findAll();
    }

    @Transactional
    BookCatalog save(BookCatalog catalog) {

        checkIsCatalogNull(catalog);

        return bookCatalogsRepository.save(catalog);
    }
    @Transactional
    void delete(BookCatalog bookCatalog) {

        checkIsCatalogNull(bookCatalog);

        bookCatalogsRepository.delete(bookCatalog);
    }

    private void checkIsCatalogNull(BookCatalog catalog) {
        if (catalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }
    }
}
