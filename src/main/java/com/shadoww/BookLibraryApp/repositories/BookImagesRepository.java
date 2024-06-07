package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.images.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookImagesRepository extends JpaRepository<BookImage, Integer> {
    Optional<BookImage> findByFilename(String filename);

    void deleteByBook_Id(int bookId);

    void deleteByFilename(String filename);

}
