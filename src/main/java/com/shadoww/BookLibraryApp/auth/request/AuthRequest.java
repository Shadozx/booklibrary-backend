package com.shadoww.BookLibraryApp.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class AuthRequest implements Serializable {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
