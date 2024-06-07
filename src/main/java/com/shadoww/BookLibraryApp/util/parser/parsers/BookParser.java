package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.models.Book;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


@Setter
public class BookParser {

    private String titleSelector;

    private String descriptionSelector;


    public Book parse(String url) throws IOException {
        Document main = ParserHelper.getDocument(url);



        Element title = main.select(titleSelector).first();
        Element description = main.select(descriptionSelector).first();

        if (title != null) {
            Book book = new Book();

            book.setTitle(title.text());

            if (description != null) {
                book.setDescription(description.text());
            }

            return book;
        }

        return null;
    }


}
