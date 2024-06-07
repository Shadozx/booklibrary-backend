package com.shadoww.BookLibraryApp.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookRatingRequest {

    private int rating;
    private int bookId;

}
