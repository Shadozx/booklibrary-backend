package com.shadoww.BookLibraryApp.models;

import com.shadoww.BookLibraryApp.models.user.Person;
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
    private int id;


    /**
     * Закладка в книжці
     *
     **/
    @ManyToOne
    @JoinColumn(name = "catalog")
    private BookCatalog catalog;


    @ManyToOne
    @JoinColumn(name = "book")
    private Book book;


    @ManyToOne
    @JoinColumn(name = "chapter")
    private Chapter chapter;


    @Min(value = 0)
    private int paragraph = 0;


    @ManyToOne
    @JoinColumn(name = "owner")
//    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private Person owner;


    @Transient
    private String url;



//    public String getUrl() {
//        return "/book/"+ book.getId() + "/ch/" + chapter.getNumberOfPage();
//    }


//    public boolean isValid() {
//        return this.book != null &&
//               this.catalog != null &&
//               this.chapter != null &&
//               this.owner != null &&
//               this.paragraph >= 0;
//    }
    @Override
    public String toString() {
        return "BookMark{" +
//                "book=" + book +
//                ", chapter=" + chapter +
                ", paragraph=" + paragraph +
                '}';
    }
}

