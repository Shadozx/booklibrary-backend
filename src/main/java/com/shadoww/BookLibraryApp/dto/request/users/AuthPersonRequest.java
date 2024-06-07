package com.shadoww.BookLibraryApp.dto.request.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@Setter
@Getter
public class AuthPersonRequest {

    private String username;

    private String password;

}
