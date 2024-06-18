package com.shadoww.BookLibraryApp.model.image;


import com.shadoww.BookLibraryApp.model.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Table(name = "book_images")
@DiscriminatorValue("Book")
@Setter
@Getter
@NoArgsConstructor
public class BookImage extends Image {

    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public BookImage(Image image) {
        this.setContentType(image.getContentType());
        this.setData(image.getData());
    }

    public void setBook(Book book) {
        if (book != null) {
            this.book = book;
            this.setFilename(book.getId() + ".jpeg");
        }
    }


}

