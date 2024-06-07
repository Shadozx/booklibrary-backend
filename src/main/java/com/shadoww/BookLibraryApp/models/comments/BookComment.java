package com.shadoww.BookLibraryApp.models.comments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.Book;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Book")
@Setter
@Getter
public class BookComment extends Comment {

    @JsonIgnore
    @OneToOne(optional = true)
    @JoinColumn(name = "book", nullable = true)
    private Book book;


}
