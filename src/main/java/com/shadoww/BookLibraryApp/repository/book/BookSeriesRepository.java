package com.shadoww.BookLibraryApp.repository.book;

import com.shadoww.BookLibraryApp.model.BookSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookSeriesRepository extends JpaRepository<BookSeries, Long> {
    boolean existsByUploadedUrl(String uploadedUrl);

    boolean existsByTitleIgnoreCase(String title);
    Optional<BookSeries> findByTitleIgnoreCase(String title);
}
