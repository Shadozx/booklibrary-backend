package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.models.images.BookImage;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.FileOutputStream;
import java.io.IOException;


public class BookImageParser {

    @Setter
    private String bookImageSelector;

    private BookImage bookImage;

    public BookImage parse(String url) throws IOException {
        Document image = ParserHelper.getDocument(url);

        Element elementLink = image.select(bookImageSelector).first();

        if (elementLink != null) {

            if (elementLink.tagName().equals("img")) {

                String imageUrl = elementLink.absUrl("src");
                this.bookImage = (BookImage) ParserHelper.parseImage(imageUrl);


                return this.bookImage;
            } else if (elementLink.tagName().equals("a")) {
                String imageUrl = elementLink.absUrl("href");
                this.bookImage = (BookImage) ParserHelper.parseImage(imageUrl);

                return this.bookImage;
            }

        }
        return null;
    }


}
