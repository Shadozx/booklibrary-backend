package com.shadoww.BookLibraryApp.models.images;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
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


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;



    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "chapter", nullable = true)
    private Chapter chapter;


//    private String contentType;
//
//
////    @Column(columnDefinition = "varchar")
//    private byte[] data;
//
//
//    private String filename;



    //    @JsonIgnore

    public ChapterImage(String filename) {
        this.setFilename(filename);
    }

    public ChapterImage(Image image) {
        this.setContentType( image.getContentType());
        this.setData(image.getData());
    }

    public void setChapterImage(Book book) {
        if (book != null) {

//            this.filename = book.getId() + "_" + (images.size() + 1) + ".jpeg";
            this.setFilename(book.getId() + "_" + UUID.randomUUID() + ".jpeg");

        }
    }

//    private static String getStandardImageUrl() {
//        return "/api/imgs/chapters/";
//    }

//    public static String getFileNameFromImg(String src) {
//        return src.startsWith(getStandardImageUrl()) ? src.replace(getStandardImageUrl(), "") : null;
//    }
//
//    public String getUrl() {
//        return getStandardImageUrl() + this.getFilename();
//    }
//
//
    @Override
    public String toString() {
        return "ChapterImage:{\n" + this.getFilename() + "}";
    }
}
