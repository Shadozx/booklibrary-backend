package com.shadoww.BookLibraryApp.service.interfaces;

import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.service.CrudService;

import java.util.List;


public interface BookSeriesService extends CrudService<BookSeries, Long> {

    BookSeries readByTitle(String title) ;

    boolean existsByTitle(String title);

    boolean existsByUrl(String url);

    List<BookSeries> getBookSeriesByBook(Long bookId);
}
