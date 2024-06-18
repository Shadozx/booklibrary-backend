package com.shadoww.BookLibraryApp.model;

import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Entity
//@Table(name = "bookmarks")
@Setter
@Getter
@NoArgsConstructor
public class BookMark implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Закладка в книжці
     *
     **/
    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private BookCatalog catalog;


    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;


    @Min(value = 0)
    private int paragraph = 0;


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
//    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Person owner;


    @Override
    public String toString() {
        return "BookMark{" +
                ", paragraph=" + paragraph +
                '}';
    }
}