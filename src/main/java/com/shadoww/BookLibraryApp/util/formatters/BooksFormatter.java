package com.shadoww.BookLibraryApp.util.formatters;


import com.shadoww.BookLibraryApp.exception.ValueAlreadyExistsException;
import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.BookImage;
import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.service.interfaces.*;
import com.shadoww.BookLibraryApp.util.parser.factories.ParserFactory;
import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BooksFormatter {

    private final AuthorService authorService;

    private final BookSeriesService bookSeriesService;

    private final BookService bookService;

    private final ImageService imageService;


    private final ChapterService chapterService;

    @Autowired
    public BooksFormatter(AuthorService authorService,
                          BookSeriesService bookSeriesService,
                          BookService bookService,
                          ChapterService chapterService,
                          ImageService imageService) {
        this.authorService = authorService;
        this.bookSeriesService = bookSeriesService;
        this.bookService = bookService;
        this.chapterService = chapterService;
        this.imageService = imageService;
    }


    public boolean format(String url) {

        if (url == null || Objects.equals(url, "")) {
            return false;
        }

        try {
            return parseUrl(url).size() > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> parseUrl(String url) throws IOException {
        System.out.println("Start parsing url");

        URL u = new URL(url);

        Parser parser = ParserFactory.createParserForHost(u.getHost());

        System.out.println(u.getHost());

        List<Book> books = new ArrayList<>();
        if (parser.canParseAuthorBooks(url)) {

            books.addAll(parseAuthorBooks(parser, url));
        } else if (parser.canParseBookSeriesBooks(url)) {

            books.addAll(parseBookSeriesBooks(parser, url));
        } else if (parser.canParseBook(url)) {

            // якщо вже колись була додана до бібліотеки книга(щоб не дублювати дані)
            if (existsBookByUrl(url)) {
                throw new ValueAlreadyExistsException("Книжка з таким посиланням вже існує!");
            }

            books.add(parseFullBook(parser, url));
        } else {
            throw new IllegalArgumentException("Цей парсер нічого не підтримує");
        }

        System.out.println("End parsing url");

        return books;
    }

    public List<Book> parseAuthorBooks(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseAuthorBooks(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг серій книг");
        }

        List<Book> books = new ArrayList<>();

        // якщо вже колись був доданий до бібліотеки автор книг(щоб не дублювати дані)
        if (existsAuthorByUrl(url)) {
            throw new ValueAlreadyExistsException("Автор з таким посиланням вже існує!");
        }

        List<String> urls = parser.parseBooksByAuthor(url);

        for (var bookUrl : urls) {

            books.add(parseFullBook(parser, bookUrl));
        }

        if (parser.canParseAuthor(url)) {

            Author parsedAuthor = parser.parseAuthor(url);

            Author author;

            if (authorService.existsByName(parsedAuthor.getName())) {

                author = authorService.readByName(parsedAuthor.getName());
                author.addAllBooks(books);
            } else {

                parsedAuthor.setUploadedUrl(url);
                parsedAuthor.addAllBooks(books);

                author = authorService.create(parsedAuthor);
            }

            System.out.println(author);
            books.forEach(book -> book.addAllAuthors(List.of(author)));

            authorService.update(author);
        }else {
            System.out.println("Парсер не підтримує парсинг самого автора");
        }

        return books;
    }

    public List<Book> parseBookSeriesBooks(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseBookSeriesBooks(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг серій книг");
        }

        List<Book> books = new ArrayList<>();

        // якщо вже колись була додана до бібліотеки серія книг(щоб не дублювати дані)
        if (existsBookSeriesByUrl(url)) {
            throw new ValueAlreadyExistsException("Серія книг з таким посиланням вже існує!");
        }

        List<String> urls = parser.parseBooksBySeries(url);

        for (var bookUrl : urls) {

            books.add(parseFullBook(parser, bookUrl));
        }

        if (parser.canParseBookSeries(url)) {
            BookSeries parsedBookSeries = parser.parseBookSeries(url);

            BookSeries bookSeries;

            if (bookSeriesService.existsByTitle(parsedBookSeries.getTitle())) {
                bookSeries = bookSeriesService.readByTitle(parsedBookSeries.getTitle());

                bookSeries.addAllBooks(books);

            } else {
                parsedBookSeries.setUploadedUrl(url);
                parsedBookSeries.addAllBooks(books);

                bookSeries = bookSeriesService.create(parsedBookSeries);
            }

            books.forEach(book -> book.addAllBookSeries(List.of(bookSeries)));

            bookSeriesService.update(bookSeries);
        }

        return books;
    }

    public Book parseFullBook(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (existsBookByUrl(url)) {
            return bookService.getByUrl(url);
        }

        Book book = parseBookDetails(parser, url);


        if (existsBookByTitle(book.getTitle())) {
            return bookService.getByTitle(book.getTitle());
        }

        BookImage bookImage = null;

        if (parser.canParseBookImage()) {
            bookImage = parser.parseBookImage(url);
        }

        book = bookService.create(book);

        if (bookImage != null) {

            book.setBookImage(bookImage);
            bookImage.setBook(book);

            imageService.create(bookImage);
        }

        parseBookChapters(parser, book, url);

        return book;
    }

    public Book parseBookDetails(Parser parser, String url) throws IOException {

        checkIsParserNull(parser);

        if (!parser.canParseBook(url)) {
            throw new IllegalArgumentException("Цей парсер не підтримує парсинг книг");
        }

        Book book = parser.parseBook(url);

        if (book == null) {
            throw new RuntimeException("Сталася помилка при парсингу книги");
        }

//        saveChapters(parser, book, url);
//        parseChapters(parser, book, url);

        return book;
    }

    public List<Chapter> parseBookChapters(Parser parser, Book book, String url) throws IOException {

        checkIsParserNull(parser);

        List<Chapter> chapters = parser.parseChapters(url, book);

        List<ChapterImage> images = parser.getImages();


        if (chapters != null) {
            book.setAmount(chapters.size());

            int chapterNumber = 1;
            for (var chapter : chapters) {
                chapter.setBook(book);

                chapter.setChapterNumber(chapterNumber);

//                System.out.println(chapter);


                chapterService.create(chapter);
                chapterNumber++;
            }

            if (images != null && !images.isEmpty()) {
                imageService.createChapterImages(images);
            }

            return chapters;
        }

        return null;
    }

    public List<Chapter> parseBookChapters(Book book) throws IOException {
        String url = book.getUploadedUrl();
        if (url == null || url.equals("")) {
            throw new IllegalArgumentException("Не існує посилання на оригінал книги!");
        }

        return parseBookChapters(
                ParserFactory.createParserForHost(new URL(url).getHost()),
                book,
                book.getUploadedUrl());
    }

    public List<Chapter> reParseBookChapters(Book book) throws IOException {

        String url = book.getUploadedUrl();

        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());

        List<Chapter> chapters = parser.parseChapters(url, book);

        if (chapters == null || chapters.isEmpty()) {
            throw new RuntimeException("Перепарсити книгу не вдалося");
        }

        List<ChapterImage> images = parser.getImages();

        chapterService.deleteByBook(book.getId());

        book.setAmount(chapters.size());

        System.out.println("Chapter size is "+ chapters.size());
        int i = 1;
        for (var c : chapters) {
            c.setBook(book);
            c.setChapterNumber(i++);

            chapterService.create(c);
        }

        bookService.update(book);

        // якщо є фотографії
        if (images != null && !images.isEmpty()) {

            imageService.deleteChaptersImages(chapterService.getBookChapters(book));
            imageService.createChapterImages(images);
        }

        return chapters;
    }
    private boolean existsBookByUrl(String url) {
        if (bookService == null) {
            return false;
        }

        return bookService.existsByUrl(url);
    }

    private boolean existsBookByTitle(String title) {

        if (bookService == null) {
            return false;
        }

        return bookService.existByTitle(title);
    }

    private boolean existsAuthorByUrl(String url) {
        if (authorService == null) {
            return false;
        }

        return authorService.existsByUrl(url);
    }

    private boolean existsBookSeriesByUrl(String url) {
        if (bookSeriesService == null) {
            return false;
        }

        return bookSeriesService.existsByUrl(url);
    }

    private boolean existsUrl(String url) {
        return existsBookByUrl(url) || existsBookSeriesByUrl(url) || existsAuthorByUrl(url);
    }

    private void checkIsParserNull(Parser parser) {
        if (parser == null) {
            throw new IllegalArgumentException("Парсер для стягування книг не повинен бути пустим!");
        }
    }

    /*public List<Chapter> parseChapters(Book book) {
        if (book != null) {

            try {
                URL url = new URL(book.getUploadedUrl());

                Parser parser = null;
                String host = url.getHost();
                if (host.equals("loveread.ec")) {
                    parser = ParserFactory.createLoveReadParser();
                } else if (host.equals("librebook.me")) {
                    parser = ParserFactory.createLibreBookParser();
                } else if (host.equals("4italka.su")) {
                    parser = ParserFactory.create4italkaParser();
                } else if (host.equals("flibusta.is")) {
                    parser = ParserFactory.createFlibustaParser();
                } else if (host.equals("rulit.me")) {
                    parser = ParserFactory.createRulitParser();
                } else if (host.equals("militera.lib")) {
                    parser = ParserFactory.createMiliteraParser();
                } else if (host.equals("coollib.net")) {
                    parser = ParserFactory.createCoolLibParser();
                }

                if (parser != null) {
                    System.out.println("RE PARSING!");
                    parser.setBook(book);

                    List<Chapter> chapters = parser.parseChapters(url.toString());


                    if (chapters != null && !chapters.isEmpty()) {


                        List<ChapterImage> images = parser.getImages();
                        chaptersService.deleteByBook(book.getId());

                        bookService.saveBook(book, chapters);

                        // якщо є фотографії
                        if (images != null && !images.isEmpty()) {
//                        imageService.deleteImages(book.getImages().stream().filter(i -> i.getId() != book.getBookImage().getId()).toList());
                            imageService.deleteChaptersImages(chaptersService.findChaptersByBook(book));
                            imageService.saveChapterImages(images);
                        }

                        return chapters;
                    }
                }

            } catch (MalformedURLException e) {
                System.out.println("Щось сталося з силкою...");

                return null;
            }
//
        }
        return null;
    }*/
}