package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterImagesRepository extends JpaRepository<ChapterImage, Integer> {

    Optional<ChapterImage> findByFilename(String filename);


    void deleteByChapter_Id(int bookId);

    void deleteByFilename(String filename);
}
