package com.shadoww.BookLibraryApp.util.responsers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseChapter {

    private ResponseChapter() {
    }

    public static ResponseEntity<?> noFound() {
        return new ResponseEntity("Такого розділу не існує", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<?> noContent() {
        return new ResponseEntity<>("Форма для розділу книги пуста", HttpStatus.BAD_REQUEST);
    }


    public static ResponseEntity<?> noContent(String message) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    public static ResponseEntity<?> exist() {
        return new ResponseEntity<>("Такий розділ книги вже існує", HttpStatus.CONFLICT);
    }

    public static ResponseEntity<?> addSuccess() {
        return new ResponseEntity<>("Цей розділ книги був успішно доданий", HttpStatus.OK);
    }


    public static ResponseEntity<?> deleteSuccess() {
        return new ResponseEntity<>("Цей розділ книги був успішно видалений", HttpStatus.OK);
    }


    public static ResponseEntity<?> updateSuccess() {
        return new ResponseEntity<>("Цей розділ книги був успішно оновлений", HttpStatus.OK);
    }

    public static ResponseEntity<?> errorServer() {
        return new ResponseEntity<>("Сталася помилка на сервері", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
