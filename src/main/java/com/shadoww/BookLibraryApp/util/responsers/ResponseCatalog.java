package com.shadoww.BookLibraryApp.util.responsers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseCatalog {
    private ResponseCatalog() {
    }

    public static ResponseEntity noFound() {
        return new ResponseEntity("Такого каталога не існує", HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity noContent() {
        return new ResponseEntity("Форма для каталога пуста", HttpStatus.BAD_REQUEST);
    }


    public static ResponseEntity exist() {
        return new ResponseEntity("Такий каталог вже існує", HttpStatus.CONFLICT);
    }

    public static ResponseEntity addSuccess() {
        return new ResponseEntity("Даний каталог був успішно доданий", HttpStatus.OK);
    }


    public static ResponseEntity deleteSuccess() {
        return new ResponseEntity("Даний каталог був успішно видалений", HttpStatus.OK);
    }

    public static ResponseEntity updateSuccess() {
        return new ResponseEntity("Даний каталог був успішно оновлений", HttpStatus.OK);
    }

    public static ResponseEntity errorServer() {
        return new ResponseEntity("Сталася помилка на сервері", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
