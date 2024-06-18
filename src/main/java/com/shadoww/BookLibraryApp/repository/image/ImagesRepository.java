package com.shadoww.BookLibraryApp.repository.image;

import com.shadoww.BookLibraryApp.model.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {

    Optional<Image> findImageByFilename(String filename);


//    void deleteImageByBook_Id(int bookId);

    void deleteImageByFilename(String filename);

//    List<Image> findByImage_type(String imageType);
}