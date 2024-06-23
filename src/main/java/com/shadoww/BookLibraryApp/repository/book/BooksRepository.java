package com.shadoww.BookLibraryApp.repository.book;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    List<Book> findByAuthors(Author authors);
    List<Book> findBySeries(BookSeries series);

    Optional<Book> findByUploadedUrl(String uploadedUrl);



//    List<Book> findTop10ByOrderByAddedDesc();

    Page<Book> findByTitleContainingIgnoreCase(Pageable page, String title);

//    Page<Book> findBooksByOrderByCreatedAtDesc(Pageable page);

    boolean existsBookByUploadedUrl(String uploadedUrl);

    boolean existsBookByTitle(String title);
//    List<Book> findFirst1OrderByAdded();
}