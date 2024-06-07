package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.parsers.BookImageParser;
import com.shadoww.BookLibraryApp.util.parser.parsers.BookParser;
import com.shadoww.BookLibraryApp.util.parser.parsers.ChapterParser;
import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import lombok.Getter;
import lombok.Setter;

@Setter
//@Getter
public class ParserBuilder {


    private BookImageParserBuilder bookImageParserBuilder;

    private BookParserBuilder bookParserBuilder;


    private ChapterParserBuilder chapterParserBuilder;



    private BooksParserBuilder booksParserBuilder;


    public ParserBuilder() {}

    public BookImageParserBuilder bookImage() {
        this.bookImageParserBuilder = new BookImageParserBuilder(this);

        return this.bookImageParserBuilder;
    }

    public BookParserBuilder book() {
        this.bookParserBuilder = new BookParserBuilder(this);

        return this.bookParserBuilder;
    }

    public ChapterParserBuilder chapters() {

        this.chapterParserBuilder = new ChapterParserBuilder(this);

        return this.chapterParserBuilder;
    }


    public BooksParserBuilder books() {
        this.booksParserBuilder = new BooksParserBuilder(this);

        return this.booksParserBuilder;
    }



    public ParserBuilder and() { return null; }


    public Parser build() {
         Parser parser = new Parser();

         if (bookParserBuilder != null)
             parser.setBookParser(bookParserBuilder.getBookParser());

         if (bookImageParserBuilder != null)
             parser.setBookImageParser(bookImageParserBuilder.getBookImageParser());

         if (chapterParserBuilder != null)
             parser.setChapterParser(chapterParserBuilder.getChapterParser());


         if (booksParserBuilder !=null)
             parser.setBooksParser(booksParserBuilder.getBooksParser());
        return parser;
    }

}
