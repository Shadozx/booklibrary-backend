package com.shadoww.BookLibraryApp.dto.request.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AuthPersonRequest {

    private String email;

    private String username;

    private String password;

}
