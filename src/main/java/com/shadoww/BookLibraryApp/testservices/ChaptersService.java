package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;

import java.util.List;

public interface ChaptersService {

    List<Chapter> findChapters();

    Chapter findByBookAndNumber(Book book, int number);

    Chapter findFirstChapterByBook(Book book);

    List<Chapter> findChaptersByBook(Book book);

    List<Chapter> findByBook(Book book);

    Chapter readById(int id);

    Chapter create(Chapter chapter);

    Chapter save(Chapter chapter);

    void update(Chapter updated);

    void update(Chapter updated, Chapter toUpdated);

    void deleteById(int id);

    void delete(Chapter chapter);

    void deleteByBook(int id);
}
