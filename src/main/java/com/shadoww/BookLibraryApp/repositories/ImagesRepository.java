package com.shadoww.BookLibraryApp.repositories;

import com.shadoww.BookLibraryApp.models.images.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Integer> {

    Optional<Image> findImageByFilename(String filename);


//    void deleteImageByBook_Id(int bookId);

    void deleteImageByFilename(String filename);

//    List<Image> findByImage_type(String imageType);
}