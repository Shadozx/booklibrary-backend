package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.AuthorParser;
import lombok.Getter;

import java.util.Arrays;

public class AuthorParserBuilder {

    @Getter
    private AuthorParser authorParser;

    private final ParserBuilder parserBuilder;


    public AuthorParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        authorParser = new AuthorParser();
    }

    public AuthorParserBuilder nameSelector(String selector) {
        authorParser.setNameSelector(selector);

        return this;
    }

    public AuthorParserBuilder biographySelector(String selector) {
        authorParser.setBiographySelector(selector);

        return this;
    }

    public AuthorParserBuilder matcher(String matcher) {
        authorParser.setMatcher(matcher);

        return this;
    }

    public AuthorParserBuilder deleteElements(String... elements) {
        authorParser.setDeleteElements(Arrays.asList(elements));

        return this;
    }

    public ParserBuilder and() {
        return parserBuilder;
    }
}