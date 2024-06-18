package com.shadoww.BookLibraryApp.repository.book;

import com.shadoww.BookLibraryApp.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Long> {
    boolean existsByUploadedUrl(String uploadedUrl);
    Optional<Author> findByName(String name);
    boolean existsByName(String name);

}
