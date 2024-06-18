//package com.shadoww.BookLibraryApp.repository.image;
//
//import com.shadoww.BookLibraryApp.model.image.BookImage;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface BookImagesRepository extends JpaRepository<BookImage, Integer> {
//    Optional<BookImage> findByFilename(String filename);
//
//    void deleteByBook_Id(int bookId);
//
//    void deleteByFilename(String filename);
//
//}
