package com.shadoww.BookLibraryApp.dto.request.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BookLinkRequest {

    @JsonProperty("url")
    String url;
}
