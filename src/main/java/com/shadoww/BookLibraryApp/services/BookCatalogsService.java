package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.repositories.BookCatalogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookCatalogsService {

    private final BookCatalogsRepository bookCatalogsRepository;

    @Autowired
    public BookCatalogsService(BookCatalogsRepository bookCatalogsRepository) {
        this.bookCatalogsRepository = bookCatalogsRepository;
    }

    public BookCatalog getByIdAndPerson(int id, Person owner) {
        return bookCatalogsRepository
                .findBookCatalogByIdAndOwner(id, owner)
                .orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }

    public BookCatalog readById(int id) {
        return bookCatalogsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого каталога не існує"));
    }


    public List<BookCatalog> findByPerson(Person owner) {

        return bookCatalogsRepository.findBookCatalogByOwner(owner);
    }

    @Transactional
    public void create(BookCatalog bookCatalog) {

        if (bookCatalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }


        bookCatalogsRepository.save(bookCatalog);
    }

    @Transactional
    public BookCatalog update(BookCatalog updatedCatalog) {

        if (updatedCatalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }

        readById(updatedCatalog.getId());

        return save(updatedCatalog);

    }

    private BookCatalog save(BookCatalog catalog) {
        if (catalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }

        return bookCatalogsRepository.save(catalog);
    }

    @Transactional
    public void deleteById(int id) {
        BookCatalog catalog = readById(id);

        delete(catalog);
    }

    @Transactional
    public void delete(BookCatalog bookCatalog) {

        if (bookCatalog == null) {
            throw new NullPointerException("Каталог не може бути пустим");
        }

        bookCatalogsRepository.delete(bookCatalog);
    }


}
