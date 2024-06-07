package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.BooksParser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BooksParserBuilder {

    private BooksParser booksParser;

    private ParserBuilder parserBuilder;
    public BooksParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        this.booksParser = new BooksParser();
    }

    public BooksParserBuilder deleteElementsAuth(String... elements) {

        this.booksParser.setForDeleteElemsAuth(new ArrayList<>(List.of(elements)));

        return this;
    }

    public BooksParserBuilder author(String authorSelector) {

        this.booksParser.setAuthorSelector(authorSelector);

        return this;
    }

    public BooksParserBuilder deleteElementsSeries(String... elements) {

        this.booksParser.setForDeleteElemsSeries(new ArrayList<>(List.of(elements)));

        return this;
    }

    public BooksParserBuilder series(String seriesSelector) {
        this.booksParser.setSeriesSelector(seriesSelector);

        return this;
    }


    public ParserBuilder and() {
        return this.parserBuilder;
    }
}
