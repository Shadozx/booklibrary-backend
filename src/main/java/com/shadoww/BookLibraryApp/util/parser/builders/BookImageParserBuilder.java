package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.BookImageParser;
import lombok.Getter;

@Getter
public class BookImageParserBuilder {

    private ParserBuilder parserBuilder;


    private BookImageParser bookImageParser;

    public BookImageParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        this.bookImageParser = new BookImageParser();
    }


    public BookImageParserBuilder selector(String bookImageSelector) {
        bookImageParser.setBookImageSelector(bookImageSelector);


        return this;
    }

    public ParserBuilder and() {

        return this.parserBuilder;
    }


}
