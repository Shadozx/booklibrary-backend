package com.shadoww.BookLibraryApp.model.comment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.model.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Book")
@Setter
@Getter
public class BookComment extends Comment {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = true)
    private Book book;


}
