package com.shadoww.BookLibraryApp.repository;

import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookMark;
import com.shadoww.BookLibraryApp.model.user.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookMarksRepository extends JpaRepository<BookMark, Long> {

    Optional<BookMark> findByBookAndOwner(Book book, Person owner);

    Optional<BookMark> findByCatalog_IdAndBook_Id(long catalogId, long bookId);


    boolean existsBookMarkByChapter_IdAndParagraph(long chapterId, int paragraph);

    Optional<BookMark> findBookMarkByChapter_IdAndParagraph(long chapterId, int paragraph);

    void deleteBookMarkByChapter_IdAndParagraph(long chapterId, int paragraph);
}
