package com.shadoww.BookLibraryApp.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class BookMarkRequest {

    private long bookId;
    private long chapterId;
    private int paragraph;


}
