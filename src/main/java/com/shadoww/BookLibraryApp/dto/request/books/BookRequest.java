package com.shadoww.BookLibraryApp.dto.request.books;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookRequest {

    private String title;

    private String description;

    private String bookImage;


    public boolean isEmpty() {
        return isTitleEmpty() && isDescriptionEmpty() && isBookImageUrlEmpty();
    }

    public boolean isTitleEmpty() {
        return title == null || title.equals("");
    }

    public boolean isDescriptionEmpty() {
        return description == null || description.equals("");
    }

    public boolean isBookImageUrlEmpty() {
        return bookImage == null || bookImage.equals("");
    }


    @Override
    public String toString() {
        return "BookForm{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
