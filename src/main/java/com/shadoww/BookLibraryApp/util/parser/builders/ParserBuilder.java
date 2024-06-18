package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import lombok.Setter;

@Setter
//@Getter
public class ParserBuilder {
    private BookParserBuilder bookParserBuilder;
    private BookImageParserBuilder bookImageParserBuilder;
    private ChapterParserBuilder chapterParserBuilder;

    private BooksParserBuilder booksParserBuilder;

    private AuthorParserBuilder authorParserBuilder;

    private BookSeriesParserBuilder bookSeriesParserBuilder;

    private String host;

    public ParserBuilder() {
    }

    public BookImageParserBuilder bookImage() {
        this.bookImageParserBuilder = new BookImageParserBuilder(this);

        return this.bookImageParserBuilder;
    }

    public BookParserBuilder book() {
        bookParserBuilder = new BookParserBuilder(this);

        return bookParserBuilder;
    }

    public BooksParserBuilder books() {
        booksParserBuilder = new BooksParserBuilder(this);
        return booksParserBuilder;
    }

    public AuthorParserBuilder author() {
        return authorParserBuilder = new AuthorParserBuilder(this);
    }

    public BookSeriesParserBuilder series() {
        return bookSeriesParserBuilder = new BookSeriesParserBuilder(this);
    }

    public ChapterParserBuilder chapters() {
        chapterParserBuilder = new ChapterParserBuilder(this);

        return chapterParserBuilder;
    }

    public ParserBuilder host(String host) {
        this.host = host;

        return this;
    }

    public Parser build() {
        Parser parser = new Parser();

        if (bookParserBuilder != null) {
            parser.setBookParser(bookParserBuilder.getBookParser());
        }
        if (bookImageParserBuilder != null) {
            parser.setBookImageParser(bookImageParserBuilder.getBookImageParser());
        }
        if (chapterParserBuilder != null) {
            parser.setChapterParser(chapterParserBuilder.getChapterParser());
        }
        if (authorParserBuilder != null) {
            parser.setAuthorParser(authorParserBuilder.getAuthorParser());
        }

        if (bookSeriesParserBuilder != null) {
            parser.setBookSeriesParser(bookSeriesParserBuilder.getBookSeriesParser());
        }

        if (host != null) {
            parser.setHost(host);
        }

        if (booksParserBuilder != null) {
            parser.setBooksParser(booksParserBuilder.getBooksParser());
        }

        return parser;
    }
}