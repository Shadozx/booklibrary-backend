package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.BookParser;
import lombok.Getter;

@Getter
public class BookParserBuilder {


    private BookParser bookParser;


    private ParserBuilder parserBuilder;

    public BookParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        this.bookParser = new BookParser();
    }

    public BookParserBuilder title(String titleSelector) {
        this.bookParser.setTitleSelector(titleSelector);

        return this;
    }


    public BookParserBuilder description(String descriptionSelector) {
        this.bookParser.setDescriptionSelector(descriptionSelector);

        return this;
    }

    public ParserBuilder and() {

        return this.parserBuilder;
    }


}
