package com.shadoww.BookLibraryApp.dto.request.books;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@Getter
@Setter
public class BookLinkRequest {

    @JsonProperty("bookUrl")
    String bookUrl;
}
