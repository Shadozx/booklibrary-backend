package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.model.Author;
import com.shadoww.BookLibraryApp.model.BookSeries;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

@Setter
public class BookSeriesParser {

    private SeriesInformationSelector seriesInformationSelector;

    @Getter
    private String matcher;

    private List<String> deleteElements;

    public boolean canParseBookSeries(String bookSeriesUrl) {

        return matcher != null && bookSeriesUrl.matches(".+" + matcher);
    }

    public BookSeries parseSeries(String seriesUrl) throws IOException {
        Document page = ParserHelper.getDocument(seriesUrl);

        if (deleteElements != null) {
            for (var e : deleteElements) {
                page.select(e).remove();
            }
        }

        Element name = page.select(seriesInformationSelector.getNameSelector()).first();
//        Element authorName = page.select(seriesInformationSelector.getAuthorNameSelector()).first();

        if (name == null) {
            throw new IllegalArgumentException("Не вдалося отримати назву серії книг");
        }

        BookSeries series = new BookSeries();
        series.setTitle(name.text());

//        if (authorName != null) {
//            Author author = new Author();
//            author.setName(authorName.text());
//
//            series.setAuthors(List.of(author));
//        }

        return series;
    }

    public void setNameSelector(String nameSelector) {
        if (seriesInformationSelector == null) {
            seriesInformationSelector = new SeriesInformationSelector();
        }

        seriesInformationSelector.setNameSelector(nameSelector);
    }

    public void setAuthorNameSelector(String authorNameSelector) {
        if (seriesInformationSelector == null) {
            seriesInformationSelector = new SeriesInformationSelector();
        }

        seriesInformationSelector.setAuthorNameSelector(authorNameSelector);
    }

    @Setter
    @Getter
    private static class SeriesInformationSelector {
        private String nameSelector;
        private String authorNameSelector;
    }
}
