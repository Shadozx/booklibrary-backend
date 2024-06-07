package com.shadoww.BookLibraryApp;

import com.shadoww.BookLibraryApp.models.images.Image;
import com.shadoww.BookLibraryApp.util.parser.parsers.ParserHelper;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class TestParser {

    @Test
    public void test() throws IOException {
        Image image = ParserHelper.parseImage("http://loveread.ec/img/photo_books/25173.jpg");

        try (FileOutputStream fos = new FileOutputStream("test.png")) {
            fos.write(image.getData());
            System.out.println("Зображення успішно збережено у файл ");
        } catch (IOException e) {
            System.out.println("Помилка під час збереження зображення: " + e.getMessage());
        }
    }
}
