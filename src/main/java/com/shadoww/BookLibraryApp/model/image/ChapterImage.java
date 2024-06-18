package com.shadoww.BookLibraryApp.model.image;


import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.Chapter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
//@Table(name = "chapter_images")

@DiscriminatorValue("Chapter")
@Setter
@Getter
@NoArgsConstructor
public class ChapterImage extends Image {

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;

    public ChapterImage(String filename) {
        this.setFilename(filename);
    }

    public ChapterImage(Image image) {
        this.setContentType( image.getContentType());
        this.setData(image.getData());
    }

    public void setChapterImage(Book book) {
        if (book != null) {
            this.setFilename(book.getId() + "_" + UUID.randomUUID() + ".jpeg");
        }
    }
}
