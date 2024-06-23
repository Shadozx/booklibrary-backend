package com.shadoww.BookLibraryApp.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AuthorRequest {

    private String name;

    private String biography;

    @Override
    public String toString() {
        return "AuthorRequest{" +
                "name='" + name + '\'' +
                ", biography='" + biography + '\'' +
                '}';
    }
}
