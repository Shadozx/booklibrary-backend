package com.shadoww.BookLibraryApp.util.responsers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUser {
    private ResponseUser() {
    }

    public static ResponseEntity noFound() {
        return new ResponseEntity("Такого користувача не існує", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity noContent() {
        return new ResponseEntity("Форма для користувача пуста", HttpStatus.NO_CONTENT);
    }

    public static ResponseEntity noContent(String message) {
        return new ResponseEntity(message, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity exist() {
        return new ResponseEntity("Такий користувач вже існує", HttpStatus.CONFLICT);
    }

    public static ResponseEntity addSuccess() {
        return new ResponseEntity("Даний користувач була успішно доданий", HttpStatus.OK);
    }


    public static ResponseEntity deleteSuccess() {
        return new ResponseEntity("Даний користувач була успішно видалений", HttpStatus.OK);
    }


    public static ResponseEntity updateSuccess() {
        return new ResponseEntity("Даний користувач була успішно оновлений", HttpStatus.OK);
    }

    public static ResponseEntity errorServer() {
        return new ResponseEntity("Сталася помилка на сервері", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
