package com.shadoww.BookLibraryApp.service.interfaces;


import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;

public interface ChapterService extends CrudService<Chapter, Long> {

    Chapter getChapterByBookAndNumber(Book book, int number);

    List<Chapter> getBookChapters(Book book);

    void deleteByBook(long id);
}
