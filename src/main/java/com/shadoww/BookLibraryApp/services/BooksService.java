package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.dto.request.books.BookFilterRequest;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.repositories.BooksRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;


    private final ImagesService imagesService;
    //
    private final ChaptersService chaptersService;

    @Autowired
    public BooksService(BooksRepository booksRepository, ImagesService imagesService, ChaptersService chaptersService) {
        this.booksRepository = booksRepository;
        this.imagesService = imagesService;
        this.chaptersService = chaptersService;
    }

    public List<Book> findBooks() {
        return booksRepository.findAll();
    }

    public List<Book> filterBooks(BookFilterRequest filterRequest) {


        return findBooks().stream()
                .filter(book -> filterRequest.getSearchText() == null || book.getTitle().toLowerCase().contains(filterRequest.getSearchText()))
                .filter(book -> filterRequest.getFromAmount() == null || book.getChapters().size() >= filterRequest.getFromAmount())
                .filter(book -> filterRequest.getToAmount() == null || book.getChapters().size() <= filterRequest.getToAmount())
                .filter(book -> filterRequest.getFromYear() == null || book.getAdded().getYear() >= filterRequest.getFromYear())
                .filter(book -> filterRequest.getToYear() == null || book.getAdded().getYear() <= filterRequest.getToYear())
                .toList();
    }

    public Book readById(int id) {

        return booksRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такої книги не існує"));
    }

    public Page<Book> findBooksByPage(int page) {
        return booksRepository.findBooksByOrderByAddedDesc(PageRequest.of(page, 20));
    }

    public boolean existByTitle(String title) {
        return booksRepository.existsBookByTitle(title);
    }

    public Page<Book> findByTitle(String bookName, int page) {
        return booksRepository.findByTitleContainingIgnoreCase(PageRequest.of(page, 20), bookName);
    }

    public Optional<Book> findByUrl(String url) {
        return booksRepository.findByUploadedUrl(url);
    }


//    public List<Book> findLastBooks() {
//        //return booksRepository.findFirst1OrderByAdded();
////        return booksRepository.findTop10(Sort.by(Sort.Direction.ASC, "added"));
//        return booksRepository.findTop10ByOrderByAddedDesc();
////        return List.of();
//    }


    public boolean exists(String uploadedUrl) {
        return booksRepository.existsBookByUploadedUrl(uploadedUrl);
    }


    @Transactional
    public Book create(Book book) {

        if (book == null) {
            throw new NullPointerException("Книга не може бути пустою");
        }


        return booksRepository.save(book);
    }


    @Transactional
    public void saveChapter(int bookId, Chapter newChapter) {
        Book book = readById(bookId);


        book.setAmount(book.getAmount() + 1);

        newChapter.setBook(book);


        chaptersService.save(newChapter);
        update(book);
    }

    @Transactional
    public void saveBookImage(Book book, BookImage bookImage) {
        if (book == null) {
            throw new NullPointerException("Книга не може бути пустою");
        }

        if (bookImage == null) {
            throw new NullPointerException("Фото книги не може бути пустим");
        }


        create(book);

        updateBookImage(book, bookImage);
    }

    @Transactional
    public void updateBookImage(Book book, BookImage bookImage) {
        book.setBookImage(bookImage);

        bookImage.setBook(book);

        imagesService.save(bookImage);

        update(book);
    }

    @Transactional
    public void saveBook(Book book, List<Chapter> chapters) {
        if (book != null && !chapters.isEmpty()) {
            book.setAmount(chapters.size());
            System.out.println("?");

            for (Chapter c : chapters) {
                c.setBook(book);
                System.out.println("Here");
                chaptersService.save(c);
            }

//            book.setChapters(chapters);


            update(book);

        } else {
            System.out.println("Глав немає");
        }
    }

    @Transactional
    public void saveBook(Book book, BookImage bookImage, List<Chapter> chapters) {

        if (book != null && bookImage != null && !chapters.isEmpty()) {

            create(book);


            book.setBookImage(bookImage);

//            bookImage.setBook(book);


            for (Chapter c : chapters) {
                c.setBook(book);
                System.out.println("Here");
                chaptersService.save(c);
            }

//            book.setChapters(chapters);

            book.setAmount(chapters.size());
            System.out.println("?");

            update(book);

            System.out.println("?");

            imagesService.save(bookImage);


        }
    }


    @Transactional
    public Book update(Book updatedBook) {

        Book oldBook = readById(updatedBook.getId());


        oldBook.setTitle(updatedBook.getTitle());
        oldBook.setDescription(updatedBook.getDescription());
        oldBook.setAmount(updatedBook.getAmount());

        return save(oldBook);

    }


    @Transactional
    Book save(Book book) {
        return booksRepository.save(book);
    }
    @Transactional
    public void updateBookAndImage(Book updated, BookImage bookImage) {

        Book forUpdate = readById(updated.getId());


        if (bookImage == null) {
            throw new NullPointerException("Фото книги не може бути пустою");
        }

        forUpdate.setBookImage(bookImage);
        bookImage.setBook(forUpdate);

        imagesService.save(bookImage);


        save(forUpdate);
    }


    @Transactional
    public void deleteById(int id) {
        Book book = readById(id);

        delete(book);
    }

    @Transactional
    void delete(Book book) {
        booksRepository.delete(book);
    }


    @Transactional
    public void deleteChapter(Chapter chapter) {

        Book book = chapter.getBook();
        if (book != null) {
            book.setAmount(book.getAmount() - 1);

            save(book);
        }

        chaptersService.deleteById(chapter.getId());
    }


    @Transactional
    public void deleteAll() {
        booksRepository.deleteAll();
    }

}
