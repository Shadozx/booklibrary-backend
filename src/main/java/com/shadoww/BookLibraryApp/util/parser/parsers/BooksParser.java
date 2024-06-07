package com.shadoww.BookLibraryApp.util.parser.parsers;


import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

@Setter
public class BooksParser {


    private String authorSelector;

    private List<String> forDeleteElemsAuth;

//    ==================================================
    private String seriesSelector;

    private List<String> forDeleteElemsSeries;



    public List<String> parseByAuthor(String authorUrl) throws Exception {
        Document authorPage = ParserHelper.getDocument(authorUrl);

        if (forDeleteElemsAuth != null && !forDeleteElemsAuth.isEmpty()) {

            for (var e : forDeleteElemsAuth) {
                authorPage.select(e).remove();
            }
        }

        Elements authorBooks = authorPage.select(authorSelector);

        for (Element e : authorBooks) {
            if (!e.tagName().equals("a")) throw new Exception("Переданий селектор не силається на книжки...");
        }


        return authorBooks.stream().map(e->e.absUrl("href")).toList();
    }

    public List<String> parseBySeries(String seriesUrl) throws Exception {

        Document seriesPage = ParserHelper.getDocument(seriesUrl);

        if (forDeleteElemsSeries != null && !forDeleteElemsSeries.isEmpty()) {

            for (var e : forDeleteElemsSeries) {
                seriesPage.select(e).remove();
            }

        }
        Elements serieBooks = seriesPage.select(seriesSelector);

        for (Element e : serieBooks) {
            if (!e.tagName().equals("a")) throw new Exception("Переданий селектор не силається на книжки...");
        }

        return serieBooks.stream().map(e->e.absUrl("href")).toList();

    }
}
