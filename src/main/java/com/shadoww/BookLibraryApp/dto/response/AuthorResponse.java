package com.shadoww.BookLibraryApp.dto.response;

import com.shadoww.BookLibraryApp.model.Author;
import lombok.Value;

@Value
public class AuthorResponse {

    long id;

    String name;

    String biography;

    String uploadedUrl;

    public AuthorResponse(Author author) {
        this.id = author.getId();
        this.name = author.getName();
        this.biography = author.getBiography();
        this.uploadedUrl = author.getUploadedUrl();
    }
}
