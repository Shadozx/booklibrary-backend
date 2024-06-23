package com.shadoww.BookLibraryApp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Назва серії книг
     **/
    @NotBlank(message = "Title of book series cannot be empty")
    @Column(unique = true, nullable = false)
    private String title;

    /**
     * Опис серії книг
     **/
    @Column(columnDefinition = "varchar")
    private String description;


    /**
     * Звідки була взята серія книг (силка) а якщо пусто то значить власноруч було додано
     **/
    @Column(name = "uploaded_url")
    private String uploadedUrl;


    /**
     * Книги, що входять до серії
     **/
    @ManyToMany(
            mappedBy = "series",
            cascade = {
                    jakarta.persistence.CascadeType.DETACH,
                    jakarta.persistence.CascadeType.MERGE,
                    jakarta.persistence.CascadeType.PERSIST,
                    jakarta.persistence.CascadeType.REFRESH
            }
    )
    private List<Book> books;


    /**
     * Автори серії
     **/
//    @ManyToMany
//    @JoinTable(
//            name = "series_authors",
//            joinColumns = @JoinColumn(name = "series_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id")
//    )
//    private List<Author> authors;
    public BookSeries(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public void addBook(Book book) {
        if (this.books == null) {
            this.books = new ArrayList<>();
        }

        this.books.add(book);
    }

    public void addAllBooks(List<Book> books) {
        if (this.books == null) {
            this.books = new ArrayList<>();
        }

        this.books.addAll(books);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSeries that = (BookSeries) o;
        return Objects.equals(id, that.id) && title.equals(that.title) && description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }

    @Override
    public String toString() {
        return "BookSeries{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + uploadedUrl + '\'' +
                '}';
    }
}