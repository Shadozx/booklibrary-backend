package com.shadoww.BookLibraryApp.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
public class Chapter implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "varchar")
    @NotBlank(message = "Title of chapter cannot be empty")
    private String title;


//    @JsonIgnore

    @Column(columnDefinition = "varchar")
    @NotBlank(message = "Text of chapter cannot be empty")
    private String text;


    //    @Column(name = "number_of_page")
//    @Min(value = '0', message = "Number of chapter must be bigger than 0")
    private int numberOfPage;


    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "book")
    private Book book;


    @JsonIgnore
    @OneToMany(mappedBy = "chapter", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private List<ChapterImage> images;

//    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Image> chapterImages;

    @JsonIgnore
    @OneToMany(mappedBy = "chapter", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.REMOVE})
    private List<BookMark> bookMarks;

    @Transient
    private String url;


    public Chapter(ChapterInstance chapterInstance) {
        this.title = chapterInstance.getTitle();
        this.text = chapterInstance.getText();
        this.setImages(this, chapterInstance.getImages());
    }

    public Chapter(int id, String title, int numberOfPage) {
        this.id = id;
        this.title = title;
        this.numberOfPage = numberOfPage;
    }


//    public void setChapterImage(Image image) {
//        if (image != null) {
//            if (this.chapterImages != null) this.chapterImages.add(image);
//            else this.chapterImages = new ArrayList<>();
//        }
//    }


    public void setImages(Chapter chapter, List<ChapterImage> images) {
        if (images != null && !images.isEmpty()) {
            this.images = images;

            images.forEach(i -> {
                i.setChapterImage(this.book);
                i.setChapter(chapter);
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return Objects.equals(title, chapter.title) && Objects.equals(text, chapter.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, text);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='\n" + text.length() + "\n'" +
                ", number='" + numberOfPage + "'" +
                '}';
    }

}
