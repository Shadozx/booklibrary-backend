package com.shadoww.BookLibraryApp.util.responsers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBookMark {
    private ResponseBookMark() {
    }

    public static ResponseEntity noFound() {
        return new ResponseEntity("Такої закладки не існує", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity noContent() {
        return new ResponseEntity("Форма для закладки пуста", HttpStatus.BAD_REQUEST);
    }


    public static ResponseEntity exist() {
        return new ResponseEntity("Така закладка вже існує", HttpStatus.CONFLICT);
    }

    public static ResponseEntity addSuccess() {
        return new ResponseEntity("Дана закладка була успішно додана", HttpStatus.OK);
    }


    public static ResponseEntity deleteSuccess() {
        return new ResponseEntity("Дана закладка була успішно видалена", HttpStatus.OK);
    }

    public static ResponseEntity errorServer() {
        return new ResponseEntity("Сталася помилка на сервері", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
