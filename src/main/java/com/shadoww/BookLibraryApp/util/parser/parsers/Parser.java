package com.shadoww.BookLibraryApp.util.parser.parsers;


import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.BookImage;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Setter
@Getter
public class Parser {

    // парсери
    private BooksParser booksParser;
    private BookImageParser bookImageParser;

    private BookParser bookParser;

    private ChapterParser chapterParser;

    private AuthorParser authorParser;

    private BookSeriesParser bookSeriesParser;

    private String host;

    // те що отримуємо в результаті парсингу
//    private Book book;

    private List<Chapter> chapters;

    private List<ChapterImage> images;

    public Book parseBook(String url) throws IOException {

        Book result = bookParser.parse(url);

        if (result != null) {

            result.setUploadedUrl(url);
        }

        return result;

    }

    public boolean canParseBook(String url) {
        if (bookParser == null) return false;

        return bookParser.canParseBook(url);
    }

    public boolean canParseAuthorBooks(String url) {
        if (booksParser == null) return false;

        return booksParser.canParseAuthor(url);
    }

    public boolean canParseBookSeriesBooks(String url) {
        if (booksParser == null) return false;

        return booksParser.canParseBookSeries(url);
    }

    public boolean canParseBookImage() {
        return bookImageParser != null;
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

    public boolean canParseAuthor(String url) {
        if (authorParser == null) {
            return false;
        }

        return authorParser.canParseAuthor(url);
    }

    public Author parseAuthor(String url) throws IOException {
        if (authorParser == null || !canParseAuthor(url)) {
            throw new IllegalArgumentException("Немає здатності парсити автора");
        }

        return authorParser.parseAuthor(url);
    }

    public boolean canParseBookSeries(String url) {
        if (bookSeriesParser == null) {
            return false;
        }

        return bookSeriesParser.canParseBookSeries(url);
    }

    public BookSeries parseBookSeries(String url) throws IOException {

        if (bookSeriesParser == null || !canParseBookSeries(url)) {
            throw new IllegalArgumentException("Немає здатності парсити серії книг");
        }

        return bookSeriesParser.parseSeries(url);
    }

    public List<Chapter> parseChapters(String url, Book book) throws IOException {

        if (chapterParser == null) {
            throw new IllegalArgumentException("Немає здатності парсити розділи книги");
        }

        if (book == null) {
            throw new IllegalArgumentException("Книга не може бути пустою!");
        }


        chapters = chapterParser.parse(url, book);

        images = chapterParser.getChapterImages();

        return chapters;
    }

    public List<String> parseBooksByAuthor(String authorUrl) throws IOException {
        if (authorParser == null) {
            throw new IllegalArgumentException("Силка на серію книг не може бути пустою!");
        }

        return booksParser.parseByAuthor(authorUrl);
    }

    public List<String> parseBooksBySeries(String seriesUrl) throws IOException {

        if (seriesUrl == null) {
            throw new IllegalArgumentException("Силка на серію книг не може бути пустою!");
        }

        return booksParser.parseBySeries(seriesUrl);
    }

}
