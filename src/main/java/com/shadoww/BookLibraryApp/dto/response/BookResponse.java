package com.shadoww.BookLibraryApp.dto.response;


import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.image.BookImage;
import lombok.Value;

@Value
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookResponse {

    long id;

    String title;

    String description;

    long amount;

    String bookImageUrl;

    String uploadedUrl;

    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.amount = book.getAmount();
        this.description = book.getDescription();

        BookImage bookImage = book.getBookImage();


        this.bookImageUrl = bookImage == null ? null : bookImage.getUrl();
        this.uploadedUrl = book.getUploadedUrl();
    }
}
