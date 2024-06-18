package com.shadoww.BookLibraryApp.model;


import com.shadoww.BookLibraryApp.model.comment.ChapterComment;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
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
    private Long id;


    @NotBlank(message = "Title of chapter cannot be empty")
    @Column(nullable = false, columnDefinition = "varchar")
    private String title;


//    @JsonIgnore


    @NotBlank(message = "Text of chapter cannot be empty")
    @Column(columnDefinition = "varchar")
    private String text;


    //    @Column(name = "number_of_page")
    @Min(value = 0, message = "Chapter number must be bigger than 0")
    private int chapterNumber;

    @ManyToOne()
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToMany(mappedBy = "chapter", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<ChapterImage> images;

//    @OneToMany(mappedBy = "chapter", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<Image> chapterImages;

    @OneToMany(mappedBy = "chapter", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<BookMark> bookMarks;

    @OneToMany(mappedBy = "chapter", orphanRemoval = true)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<ChapterComment> comments;

    public Chapter(ChapterInstance chapterInstance) {
        this.title = chapterInstance.getTitle();
        this.text = chapterInstance.getText();
        this.setImages(this, chapterInstance.getImages());
    }

    public Chapter(long id, String title, int numberOfPage) {
        this.id = id;
        this.title = title;
        this.chapterNumber = numberOfPage;
    }


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
                ", number='" + chapterNumber + "'" +
                '}';
    }
}
