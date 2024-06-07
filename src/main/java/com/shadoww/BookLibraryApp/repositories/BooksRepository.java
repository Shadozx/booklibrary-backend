package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByUploadedUrl(String uploadedUrl);



//    List<Book> findTop10ByOrderByAddedDesc();

    Page<Book> findByTitleContainingIgnoreCase(Pageable page, String title);

    Page<Book> findBooksByOrderByAddedDesc(Pageable page);

    boolean existsBookByUploadedUrl(String uploadedUrl);

    boolean existsBookByTitle(String title);
//    List<Book> findFirst1OrderByAdded();
}