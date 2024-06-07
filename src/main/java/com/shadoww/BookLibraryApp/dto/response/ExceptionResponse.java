package com.shadoww.BookLibraryApp.dto.response;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ExceptionResponse {

    LocalDateTime timestamp;
    int status;
    String error;
    String message;
    String path;
}
