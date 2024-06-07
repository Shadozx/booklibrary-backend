package com.shadoww.BookLibraryApp.util.parser.parsers;


import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
@Setter
@Getter
public class Parser {

    // парсери

    private BookImageParser bookImageParser;

    private BookParser bookParser;

    private ChapterParser chapterParser;

    private BooksParser booksParser;


    // те що отримуємо після парсингу

    private Book book;

    private List<Chapter> chapters;

    private List<ChapterImage> images;



    public Book parseBook(String url) {
        try {
            Book result = bookParser.parse(url);

            if (result != null) {
                this.book = result;
                this.book.setUploadedUrl(url);
            }

            return this.book;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public BookImage parseBookImage(String url) {
        try {
            if (bookImageParser == null) return null;

            return bookImageParser.parse(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }


    public List<Chapter> parseChapters(String url) {
        try {
            if (book != null){

                this.chapters = chapterParser.parse(url, book);

                this.images = chapterParser.getChapterImages();

                return this.chapters;
            }
        } catch (IOException e) {
            System.out.println("Сталася помилка при парсингу тексту книжки...");
            System.out.println(e.getMessage());
        }

        return null;
    }

    public List<String> parseBooksByAuthor(String authorUrl) {
        if (this.booksParser != null) {
            try {
                return booksParser.parseByAuthor(authorUrl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public List<String> parseBooksBySeries(String seriesUrl) {
        if (this.booksParser != null) {
            try {
                return booksParser.parseBySeries(seriesUrl);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

}
