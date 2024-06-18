package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.BooksParser;
import lombok.Getter;

import java.util.Arrays;

@Getter
public class BooksParserBuilder {

    private final BooksParser booksParser;
    private final ParserBuilder parentBuilder;

    public BooksParserBuilder(ParserBuilder parentBuilder) {
        this.parentBuilder = parentBuilder;
        this.booksParser = new BooksParser();
    }

    public AuthorBooksBuilder authorBooks() {
        return new AuthorBooksBuilder(booksParser, this);
    }

    public SeriesBooksBuilder seriesBooks() {
        return new SeriesBooksBuilder(booksParser, this);
    }


    public ParserBuilder and() {
        return parentBuilder;
    }


    public static class AuthorBooksBuilder {
        private final BooksParser booksParser;
        private final BooksParserBuilder parentBuilder;

        public AuthorBooksBuilder(BooksParser booksParser, BooksParserBuilder parentBuilder) {
            this.booksParser = booksParser;
            this.parentBuilder = parentBuilder;
        }

        public AuthorBooksBuilder selector(String selector) {
            booksParser.setAuthorBooksSelector(selector);
            return this;
        }

        public AuthorBooksBuilder matchers(String... matchers) {
            booksParser.setAuthorBooksMatchers(matchers);
            return this;
        }

        public AuthorBooksBuilder deleteElements(String... elements) {
            booksParser.setAuthorBooksDeleteElements(Arrays.asList(elements));
            return this;
        }

        public BooksParserBuilder back() {
            return parentBuilder;
        }
    }

    public static class SeriesBooksBuilder {

        private final BooksParser booksParser;
        private final BooksParserBuilder parentBuilder;

        public SeriesBooksBuilder(BooksParser booksParser, BooksParserBuilder parentBuilder) {
            this.booksParser = booksParser;
            this.parentBuilder = parentBuilder;
        }

        public SeriesBooksBuilder selector(String selector) {
            booksParser.setSeriesBooksSelector(selector);
            return this;
        }

        public SeriesBooksBuilder matchers(String... matchers) {
            booksParser.setSeriesBooksMatchers(matchers);
            return this;
        }

        public SeriesBooksBuilder deleteElements(String... elements) {
            booksParser.setSeriesBooksDeleteElements(Arrays.asList(elements));
            return this;
        }

        public BooksParserBuilder back() {
            return parentBuilder;
        }
    }
}