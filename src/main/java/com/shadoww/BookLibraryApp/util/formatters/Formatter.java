package com.shadoww.BookLibraryApp.util.formatters;


import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.BookImage;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.ChaptersService;
import com.shadoww.BookLibraryApp.services.ImagesService;
import com.shadoww.BookLibraryApp.util.parser.factories.ParserFactory;
import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class Formatter {

    private BooksService booksService;

    private ImagesService imagesService;


    private ChaptersService chaptersService;

    @Autowired
    public Formatter(BooksService booksService, ChaptersService chaptersService, ImagesService imagesService) {
        this.booksService = booksService;
        this.chaptersService = chaptersService;
        this.imagesService = imagesService;
    }


    public boolean format(String url) {

        if (url == null || Objects.equals(url, "")) return false;


        List<String> urls = Arrays.stream(url
                        .replaceAll("\\s+", " ")
                        .split("[\\,\\.]?\s+?"))
                .toList();

        System.out.println(urls);
        if (!urls.isEmpty()) {
            urls.forEach(this::parseUrl);

            return true;
        } else {

            return parseUrl(url);
        }

    }


    private boolean parseUrl(String url) {
        System.out.println("Here");

        return addBookByUrl(url);
    }

    private boolean addBookByUrl(String bookUrl) {
        try {
            URL url = new URL(bookUrl);

            String host = url.getHost();

            Optional<Book> existedBook = booksService.findByUrl(bookUrl);
            if (existedBook.isPresent()) return true;


            else if (host.equals("loveread.ec") || host.equals("loveread.me")) {

                Parser parser = ParserFactory.createLoveReadParser();

                if (host.equals("loveread.me")) {
                    url = new URL(UriComponentsBuilder.fromHttpUrl(url.toString()).host("loveread.ec").build().toString());
                }

                // книга
                if (url.getPath().equals("/view_global.php") && url.getQuery().matches("id=\\d+")) {

                    Book book = addAllBook(parser, url.toString());

                    if (book != null) return true;

                }

                // через автора
                else if (url.getPath().equals("/books.php") && url.getQuery().matches("id_author=\\d+")) {
                    List<String> bookUrls = parser.parseBooksByAuthor(url.toString());

                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;
                }

                // серія
                else if (url.getPath().equals("/series-books.php") && url.getQuery().matches("id=\\d+")) {
                    List<String> bookUrls = parser.parseBooksBySeries(bookUrl);


                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;
                }

            } else if (host.equals("librebook.me")) {

                Parser parser = ParserFactory.createLibreBookParser();


                // через автора
                if (url.getPath().matches("\\/list\\/person\\/.+")) {

                    List<String> booksUrls = parser.parseBooksByAuthor(url.toString());
                    for (var u : booksUrls) addBookByUrl(u);

                }
                // книжка
                else if (url.getPath().matches("\\/.+")) {
                    Book book = addAllBook(parser, url.toString());

                    if (book != null) return true;
                }
            }
            else if (host.equals("flibusta.is")) {
                Parser parser = ParserFactory.createFlibustaParser();

                // книжка
                if (url.getPath().matches("\\/b\\/\\d+")) {
                    System.out.println("Flibusta book:" + url);

                    Book book = addAllBook(parser, url.toString());

                    if (book != null) return true;
                }

                // через сторінку автора
                else if (url.getPath().matches("\\/a\\/\\d+")) {
                    List<String> bookUrls = parser.parseBooksBySeries(bookUrl);

                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;

                }
            } else if (host.equals("rulit.me")) {
                Parser parser = ParserFactory.createRulitParser();

                // книжка
                if (url.getPath().matches("\\/books\\/.+\\.html") || url.getPath().matches("\\/author\\/.+\\/.+\\.html")) {
                    addAllBook(parser, url.toString());

                }
                // через автора
                else if (url.getPath().matches("\\/author\\/.+")) {
                    List<String> bookUrls = parser.parseBooksByAuthor(url.toString());

                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;

                }

            } else if (host.equals("militera.lib")) {

                Parser parser = ParserFactory.createMiliteraParser();

                // книжка
                if (url.getPath().matches("\\/.+\\/.+\\/index\\.html")) {
                    Book book = parser.parseBook(bookUrl);

                    if (book != null) {
                        booksService.create(book);

                        List<Chapter> chapters = parser.parseChapters(bookUrl);

                        List<ChapterImage> images = parser.getImages();
                        if (chapters != null) {
                            booksService.saveBook(book, chapters);

                            if (images != null && !images.isEmpty()) {
                                imagesService.saveChapterImages(images);
                            }

                        }
                        return true;
                    }
                }
            } else if (host.equals("coollib.net")) {
                Parser parser = ParserFactory.createCoolLibParser();

                // книжка
                if (url.getPath().matches("\\/b\\/.+")) {
                    Book book = addAllBook(parser, url.toString());

                    return book != null;
                }

                // через автора
                else if (url.getPath().matches("\\/a\\/.+")) {
                    List<String> bookUrls = parser.parseBooksByAuthor(bookUrl);

                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;

                }
            } else if (host.equals("4italka.su")) {


                Parser parser = ParserFactory.create4italkaParser();

                if (url.getPath().matches("\\/\\w+\\/\\w+\\/\\d+\\.htm")) {
                    String newUrl = url.toString();
                    int i = newUrl.indexOf(".htm");
                    url = new URL(newUrl.substring(0, i) + "/fulltext" + newUrl.substring(i));
                }

                // кнжика
                if (url.getPath().matches("\\/\\w+\\/\\w+\\/\\d+\\/fulltext\\.htm")) {

                    BookImage bookImage = parser.parseBookImage(url.toString());

                    Book book = parser.parseBook(url.toString());
                    if (bookImage == null) {
                        if (book != null) {
//                    booksService.saveBookImage(book, bookImage);
                            booksService.create(book);
                            List<Chapter> chapters = saveChapters(parser, book, url.toString());
                            return chapters != null && !chapters.isEmpty();
                        }

                    } else {
                        if (book != null) {
//                    booksService.saveBookImage(book, bookImage);
                            booksService.saveBookImage(book, bookImage);
                            List<Chapter> chapters = saveChapters(parser, book, url.toString());
                            return chapters != null && !chapters.isEmpty();
                        }
                    }

//                    return addAllBookWithoutBookImage(parser, url.toString());
                }

                // через автора
                else if (url.getPath().matches("\\/author\\/.+\\.htm")) {

                    List<String> bookUrls = parser.parseBooksByAuthor(url.toString());


                    for (var u : bookUrls) {
                        addBookByUrl(u);
                    }

                    if (!bookUrls.isEmpty()) return true;

                }

            }

        } catch (MalformedURLException e) {

            System.out.println("Схоже це не силка...");
            System.out.println("Message: " + e.getMessage());
            return false;
        }


        return false;
    }


    // фотографія книжки, книжка, глави
    private Book addAllBook(Parser parser, String url) {
        BookImage bookImage = parser.parseBookImage(url);

        if (bookImage != null) {
            Book book = parser.parseBook(url);
            if (book != null) {
//                    booksService.saveBookImage(book, bookImage);
                booksService.saveBookImage(book, bookImage);
                saveChapters(parser, book, url);
                return book;
            }
        }
        return null;
    }

    private Book addAllBookWithoutBookImage(Parser parser, String url) {
        Book book = parser.parseBook(url);
        if (book != null) {
            booksService.create(book);
            saveChapters(parser, book, url);
            return book;
        }


        return null;
    }

    private List<Chapter> saveChapters(Parser parser, Book book, String url) {
        List<Chapter> chapters = parser.parseChapters(url);

        List<ChapterImage> images = parser.getImages();


        if (chapters != null) {
            booksService.saveBook(book, chapters);

            System.out.println(book);
            if (images != null && !images.isEmpty()) imagesService.saveChapterImages(images);

            return chapters;
        }

        return null;
    }

    public List<Chapter> parseChapters(Book book) {
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

                        booksService.saveBook(book, chapters);

                        // якщо є фотографії
                        if (images != null && !images.isEmpty()) {
//                        imagesService.deleteImages(book.getImages().stream().filter(i -> i.getId() != book.getBookImage().getId()).toList());
                            imagesService.deleteChaptersImages(chaptersService.findChaptersByBook(book));
                            imagesService.saveChapterImages(images);
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
    }
}