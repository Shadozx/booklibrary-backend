package com.shadoww.BookLibraryApp.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
public class Book implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * Назва книжки
     **/

    @NotBlank(message = "Title of book cannot be empty")
    private String title;
    /**
     * Опис книжки
     **/

    @Column(columnDefinition = "varchar")
    @JsonIgnore
    private String description;
    /**
     * Кількість глав книжки
     **/

    @Min(value = 0, message = "Amount of chapters must be bigger than 0")
    private int amount;
    /**
     * Звідки була взята книжка (силка) а якщо пусто то значить власноруч було додано
     **/

//    @Column(name = "uploaded_url")
//    @NotBlank(message = "Uploaded url of book cannot be empty")
    private String uploadedUrl;
    /**
     * Коли була добавлена книжка
     **/

    @JsonIgnore
    @CreationTimestamp
    private Date added;
    /**
     * Фотографія книжки
     */
    @JsonIgnore
    @OneToOne()
    @JoinColumn(name = "book_image")
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private BookImage bookImage;


    /**
     * Глави книжки
     **/
    @JsonIgnore
    @OneToMany(mappedBy = "book", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    List<Chapter> chapters;


    /**
     * Силка на книжку на сайті
     **/
    @Transient
    private String link;


    public Book(String title, String description, String uploadedUrl) {
        this.title = title;
        this.description = description;
        this.uploadedUrl = uploadedUrl;
    }


    public String getLink() {
        return "/book/" + id;
    }

    public void setChapters(List<Chapter> chapters) {
        if (chapters != null) {
            this.chapters = chapters;
            this.amount = chapters.size();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && amount == book.amount && title.equals(book.title) && description.equals(book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, amount);
    }

    @Override
    public String toString() {
        return "Book{" +
                ", title='" + title + '\'' +
                " description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
