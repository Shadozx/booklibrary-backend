package com.shadoww.BookLibraryApp;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookSeries;
import com.shadoww.BookLibraryApp.model.Chapter;
import com.shadoww.BookLibraryApp.model.image.Image;
import com.shadoww.BookLibraryApp.service.interfaces.*;
import com.shadoww.BookLibraryApp.util.formatters.BooksFormatter;
import com.shadoww.BookLibraryApp.util.parser.factories.ParserFactory;
import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import org.apache.hc.core5.util.Asserts;
//import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class BooksFormatterTests {

    @Mock
    private BookService bookService;

    @Mock
    private ChapterService chapterService;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookSeriesService bookSeriesService;

    @Mock
    private ImageService imageService;

    @InjectMocks
    private BooksFormatter booksFormatter;


    @BeforeEach
    public void setUp() {
        when(bookService.create(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);
        when(chapterService.create(any(Chapter.class))).thenAnswer(i -> i.getArguments()[0]);
        when(bookSeriesService.create(any(BookSeries.class))).thenAnswer(i -> i.getArguments()[0]);
        when(authorService.create(any(Author.class))).thenAnswer(i -> i.getArguments()[0]);
        when(imageService.create(any(Image.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setId(10L);
        book.setTitle("test book");
        book.setAmount(10);

        return book;
    }

    @Test
    public void test() throws MalformedURLException {
        URL url = new URL("http://loveread.ec/view_global.php?id=6972");

        System.out.println(url.getHost().equals("loveread.ec"));

        System.out.println(url.getPath().equals("/view_global.php"));

        System.out.println(url.getQuery().matches("id=\\d+"));


        System.out.println(url.toString().matches(".+\\/view_global\\.php\\?id=\\d+"));
    }

    @Test
    public void testLovereadBook() throws IOException {


        String url = "http://loveread.ec/view_global.php?id=6972";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 1;
        int actual = books.size();
        assertEquals(expected, actual, "Має бути одна книжка!");
    }

    @Test
    public void testLovereadAuthorBooks() throws IOException {
        String url = "http://loveread.ec/biography-author.php?author=Mark-Avreliy-Antonin";

        List<Book> books = booksFormatter.parseUrl(url);

        Asserts.check(!books.isEmpty(), "Список книг не повинен бути пустим!");
        Asserts.check(books.size() == 2, "Список книг не є рівним 2!");
    }

    @Test
    public void testLovereadAuthor() throws IOException {
        String url = "http://loveread.ec/biography-author.php?author=Mark-Avreliy-Antonin";

        List<Book> books = booksFormatter.parseUrl(url);

        Asserts.check(!books.isEmpty(), "Список книг не повинен бути пустим!");
        Asserts.check(books.size() == 2, "Список книг не є рівним 2!");
        var authors = books.stream().findFirst().get().getAuthors();

        assertNotEquals(authors, null);
    }

    @Test
    public void testLovereadBookSeries() throws IOException {
        String url = "http://loveread.ec/series-books.php?id=814";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 10;
        int actual = books.size();

        Asserts.check(!books.isEmpty(), "Список книг не повинен бути пустим!");
        assertEquals(expected, actual, String.format("Список книг не є рівним %d!", expected));
        var bookSeries = books.stream().findFirst().get().getSeries();

        assertNotEquals(bookSeries, null);
        Asserts.check(!bookSeries.isEmpty(), "Список серій книг є пустим!");
    }

    @Test
    public void testLovereadBookSeriesBooks() throws IOException {
        String url = "http://loveread.ec/series-books.php?id=814";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 10;
        int actual = books.size();

        Asserts.check(!books.isEmpty(), "Список книг є пустим!");
        assertEquals(expected, actual, String.format("Список книг не є рівним %d!", expected));
    }

    @Test
    public void testLibrebookBook() throws IOException {
        List<Book> books = booksFormatter.parseUrl("https://librebook.me/the_prince/");

        int expected = 1;
        int actual = books.size();
        assertEquals(expected, actual, "Має бути одна книжка!");
    }

    @Test
    public void testCoollibNetBook() throws IOException {
        String url = "https://coollib.net/b/450181-adrian-goldsuorsi-yuliy-tsezar-polkovodets-imperator-legenda";

        List<Book> books = booksFormatter.parseUrl(url);

        System.out.println(books);
        int expected = 1;
        int actual = books.size();
        assertEquals(expected, actual, "Має бути одна книжка!");
    }

    @Test
    public void testCoollibAuthorBooks() throws IOException {
        String url = "https://coollib.net/a/188490-adrian-goldsuorsi";

        List<Book> books = booksFormatter.parseUrl(url);

        System.out.println(books);

        Asserts.check(!books.isEmpty(), "Список книг не повинен бути пустим!");
        Asserts.check(books.size() == 2, "Список книг не є рівним 2!");
    }

    @Test
    public void test4italkaBook() throws IOException {
        String url = "https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533/fulltext.htm";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 1;
        int actual = books.size();
        assertEquals(expected, actual, "Має бути одна книжка!");
    }

    @Test
    public void testFlibustaSiteBook() throws IOException {
        String url = "http://flibusta.site/b/648733";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 1;
        int actual = books.size();

        assertEquals(expected, actual, "Має бути одна книга!");
    }

    @Test
    public void testFlibustaSiteAuthorBooks() throws IOException {
        String url = "http://flibusta.site/a/168916";

        List<Book> books = booksFormatter.parseUrl(url);

        int expected = 11;
        int actual = books.size();

        Asserts.check(!books.isEmpty(), "Список книг не повинен бути пустим!");
        assertEquals(expected, actual, String.format("Список книг не є рівним %d!", expected));
    }

    @Test
    public void testWithNoWorkingUrl() {
        String url = "http://flibusta.site/b/648733fusf";

        assertThrows(IllegalArgumentException.class, () -> booksFormatter.parseUrl(url), "Парсер не повинен опрацьовувати такі посилання!");
    }

    @Test
    public void testWithInvalidUrl() {
        String url = "flibusta.site/b/648733";

        assertThrows(MalformedURLException.class, () -> booksFormatter.parseUrl(url), "Парсер не повинен опрацьовувати некоректні посилання!");
    }

    @Test
    public void testWithNoExistingHostUrl() {
        String url = "http://fl4af.bf/b/648733fusf";

        assertThrows(IllegalArgumentException.class, () -> booksFormatter.parseUrl(url), "Парсер не повинен опрацьовувати хости що не додані!");
    }

    private static Stream<Arguments> bookSeriesProvider() {
        return Stream.of(
                Arguments.of("http://loveread.ec/series-books.php?id=814", 10),
                Arguments.of("https://coollib.net/s/54885", 6),
                Arguments.of("https://flibusta.site/s/14225", 18)
        );
    }

    @ParameterizedTest
    @MethodSource("bookSeriesProvider")
    public void testBookSeriesBooksCount(String url, int expectedBooksCount) throws IOException {

        Book book = createTestBook();

        when(bookService.getByUrl(anyString())).thenReturn(book);
        when(bookService.existsByUrl(anyString())).thenReturn(true);

        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());
        List<Book> books = booksFormatter.parseBookSeriesBooks(parser, url);

        System.out.println(books.size());

        assertEquals(expectedBooksCount, books.size(), String.format("Кількість книг серії не збігаються! Має бути %s, а є - %s", expectedBooksCount, books.size()));
    }

    private static Stream<Arguments> authorsBooksProvider() {
        return Stream.of(
                Arguments.of("http://loveread.ec/biography-author.php?author=Mark-Avreliy-Antonin", 2),
                Arguments.of("https://librebook.me/list/person/nikolo_makiavelli", 10),
                Arguments.of("http://flibusta.site/a/168916", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("authorsBooksProvider")
    public void testAuthorsBooksCount(String url, int expectedBooksCount) throws IOException {

        Book book = createTestBook();

        when(bookService.getByUrl(anyString())).thenReturn(book);
        when(bookService.existsByUrl(anyString())).thenReturn(true);

        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());
        List<Book> books = booksFormatter.parseAuthorBooks(parser, url);

        System.out.println(books.size());

        assertEquals(expectedBooksCount, books.size(), String.format("Кількість книг автора не збігаються! Має бути %s, а є - %s", expectedBooksCount, books.size()));
    }

    private static Stream<Arguments> chaptersProvider() {
        return Stream.of(
                Arguments.of("https://coollib.net/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.xyz/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.in/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
                Arguments.of("https://coollib.cc/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", 131),
//                Arguments.of("http://loveread.ec/view_global.php?id=67498", 34),
//                Arguments.of("https://librebook.me/the_prince/", 27),
//                Arguments.of("https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533/fulltext.htm", 53),
                Arguments.of("http://flibusta.site/b/671697", 2)
//                Arguments.of("https://rulit.me/books/dolina-uzhasa-download-101113.html", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("chaptersProvider")
    public void testChapters(String url, int expectedAmountChapters) throws IOException {
        Parser parser = ParserFactory.createParserForHost(new URL(url).getHost());

        Book book = createTestBook();
        List<Chapter> chapters = booksFormatter.parseBookChapters(parser, book, url);

        System.out.println(chapters.size());

        assertEquals(expectedAmountChapters, chapters.size(), String.format("Кількість розділів книги не збігаються! Має бути '%s' а є - '%s'", expectedAmountChapters, chapters.size()));
    }

    private static Stream<Arguments> booksProvider() {
        // 4italka.su та rulit.me можуть не працювати - нормально. залежить на якому інтернеті парсити
        //
        return Stream.of(
                Arguments.of("http://loveread.ec/view_global.php?id=6972", "Приключения Шерлока Холмса"),
//                Arguments.of("http://loveread.me/view_global.php?id=6972", "Приключения Шерлока Холмса")
                Arguments.of("https://librebook.me/the_prince/", "Государь"),
                Arguments.of("https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533/fulltext.htm", "«Менеджер Мафии. Руководство для корпоративного Макиавелли»"),
                Arguments.of("https://4italka.su/nauka_obrazovanie/delovaya_literatura/3533.htm", "«Менеджер Мафии. Руководство для корпоративного Макиавелли»"),
                Arguments.of("https://coollib.net/b/450181-adrian-goldsuorsi-yuliy-tsezar-polkovodets-imperator-legenda", "Юлий Цезарь. Полководец, император, легенда [Адриан Голдсуорси] (fb2)"),
//                Arguments.of("https://coollib.cc/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", "Моральні листи до Луцілія [Луцій Анней Сенека] (fb2)"),
                Arguments.of("https://coollib.in/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", "Моральні листи до Луцілія [Луцій Анней Сенека] (fb2)"),
                Arguments.of("https://coollib.xyz/b/489485-lutsiy-anney-seneka-moralni-listi-do-lutsiliya", "Моральні листи до Луцілія [Луцій Анней Сенека] (fb2)"),
                Arguments.of("http://flibusta.site/b/648733", "История Французской революции. Том 1 (fb2)"),
                Arguments.of("https://rulit.me/series/sho-opyat/vtorzhenie-download-229213.html", "Вторжение"),
                Arguments.of("https://www.rulit.me/author/grin-robert/master-igry-download-399293.html", "Мастер Игры [Mastery - ru][calibre 2.45.0]"),
                Arguments.of("https://rulit.me/books/master-igry-download-399293.html", "Мастер Игры [Mastery - ru][calibre 2.45.0]")
        );
    }

    @ParameterizedTest
    @MethodSource("booksProvider")
    public void testParseBook(String url, String expectedTitle) throws IOException {
        String host = new URL(url).getHost();

        Parser parser = ParserFactory.createParserForHost(host);
        Book book = booksFormatter.parseBookDetails(parser, url);

//        System.out.println(book);

        assertEquals(expectedTitle, book.getTitle(), String.format("Назви не збігаються! Має бути '%s', а є - '%s'", expectedTitle, book.getTitle()));
    }
}
