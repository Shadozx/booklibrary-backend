package com.shadoww.BookLibraryApp.model;

import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class BookRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "Book rating must have minimum 1")
    @Max(value = 5, message = "Book rating must have maximum 5")
    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;
}
