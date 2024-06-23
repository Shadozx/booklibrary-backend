package com.shadoww.BookLibraryApp.model;


import com.shadoww.BookLibraryApp.model.comment.BookComment;
import com.shadoww.BookLibraryApp.model.image.BookImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва книжки
     **/
    @NotBlank(message = "Title of book cannot be empty")
    @Column(unique = true, nullable = false)
    private String title;

    /**
     * Опис книжки
     **/
    @Column(columnDefinition = "varchar")
    private String description;

    /**
     * Кількість глав книжки
     **/
    @Min(value = 0, message = "Amount of chapters must be bigger than 0")
    private int amount;

    /**
     * Звідки була взята книжка (силка) а якщо пусто то значить власноруч було додано
     **/
    @Column(name = "uploaded_url")
    private String uploadedUrl;

    /**
     * Коли була добавлена книжка
     **/
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Фотографія книжки
     */
    @OneToOne
    @JoinColumn(name = "image_id")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private BookImage bookImage;

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private List<BookMark> bookMarks = new ArrayList<>();

    /**
     * Глави книжки
     **/
    @OneToMany(mappedBy = "book", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<BookRating> bookRatings = new ArrayList<>();

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = {jakarta.persistence.CascadeType.REMOVE})
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<BookComment> comments = new ArrayList<>();

    /**
     * Серії, до яких належить книжка
     **/
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    jakarta.persistence.CascadeType.DETACH,
                    jakarta.persistence.CascadeType.MERGE,
                    jakarta.persistence.CascadeType.PERSIST,
                    jakarta.persistence.CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "book_series_books",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "series_id", referencedColumnName = "id")
    )
    private List<BookSeries> series;

    /**
     * Автори книжки
     **/
    @ManyToMany(
            cascade = {
                    jakarta.persistence.CascadeType.DETACH,
                    jakarta.persistence.CascadeType.MERGE,
                    jakarta.persistence.CascadeType.PERSIST,
                    jakarta.persistence.CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    public Book(String title, String description, String uploadedUrl) {
        this.title = title;
        this.description = description;
        this.uploadedUrl = uploadedUrl;
    }

    public void setChapters(List<Chapter> chapters) {
        if (chapters != null) {
            this.chapters = chapters;
            this.amount = chapters.size();
        }
    }

    public void addAuthor(Author author) {
        if (this.authors == null) {
            this.authors = new ArrayList<>();
        }

        this.authors.add(author);
    }

    public void addAllAuthors(List<Author> authors) {
        if (this.authors == null) {
            this.authors = new ArrayList<>();
        }

        this.authors.addAll(authors);
    }

    public void addBookSeries(BookSeries bookSeries) {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }

        this.series.add(bookSeries);
    }

    public void addAllBookSeries(List<BookSeries> bookSeries) {
        if (this.series == null) {
            this.series = new ArrayList<>();
        }

        this.series.addAll(bookSeries);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && amount == book.amount && title.equals(book.title) && description.equals(book.description);
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
                ", amount=" + amount + '\'' +
//                ", url='" + uploadedUrl +
                '}';
    }
}
