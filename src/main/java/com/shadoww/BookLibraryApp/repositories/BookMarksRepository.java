package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMarksRepository extends JpaRepository<BookMark, Integer> {

    Optional<BookMark> findByBookAndOwner(Book book, Person owner);

    Optional<BookMark> findByCatalog_IdAndBook_Id(int catalogId, int bookId);


    boolean existsBookMarkByChapter_IdAndParagraph(int chapterId, int paragraph);

    Optional<BookMark> findBookMarkByChapter_IdAndParagraph(int chapterId, int paragraph);

    void deleteBookMarkByChapter_IdAndParagraph(int chapterId, int paragraph);
}
