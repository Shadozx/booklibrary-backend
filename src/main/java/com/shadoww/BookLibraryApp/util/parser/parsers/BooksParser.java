package com.shadoww.BookLibraryApp.util.parser.parsers;


import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Setter
public class BooksParser {

    private AuthorBooksSelector authorBooksSelector;
    private SeriesBooksSelector seriesBooksSelector;

    public boolean canParseAuthor(String authorUrl) {

        List<String> matchers = authorBooksSelector.getMatchers();

        for (var matcher : matchers) {

            if (authorUrl.matches(".+" + matcher)) {
                return true;
            }
        }

        return false;
    }

    public List<String> parseByAuthor(String authorUrl) throws IOException {
        return getBooksUrls(authorUrl, authorBooksSelector.getDeleteElements(), authorBooksSelector.getBooksSelector());
    }

    public boolean canParseBookSeries(String bookSeriesUrl) {

        List<String> matchers = seriesBooksSelector.getMatchers();

        for (var matcher : matchers) {

            if (bookSeriesUrl.matches(".+" + matcher)) {
                return true;
            }
        }

        return false;
    }

    public List<String> parseBySeries(String seriesUrl) throws IOException {
        return getBooksUrls(seriesUrl, seriesBooksSelector.getDeleteElements(), seriesBooksSelector.getBooksSelector());
    }

    public void setAuthorBooksSelector(String booksSelector) {
        if (authorBooksSelector == null) {
            authorBooksSelector = new AuthorBooksSelector();
        }
        authorBooksSelector.setBooksSelector(booksSelector);
    }

    public void setSeriesBooksSelector(String booksSelector) {
        if (seriesBooksSelector == null) {
            seriesBooksSelector = new SeriesBooksSelector();
        }
        seriesBooksSelector.setBooksSelector(booksSelector);
    }

    public void setAuthorBooksMatchers(String... matchers) {
        if (authorBooksSelector == null) {
            authorBooksSelector = new AuthorBooksSelector();
        }
        authorBooksSelector.setMatchers(Arrays.asList(matchers));
    }

    public void setSeriesBooksMatchers(String... matchers) {
        if (seriesBooksSelector == null) {
            seriesBooksSelector = new SeriesBooksSelector();
        }
        seriesBooksSelector.setMatchers(Arrays.asList(matchers));
    }

    public void setAuthorBooksDeleteElements(List<String> forDeleteElems) {
        if (authorBooksSelector == null) {
            authorBooksSelector = new AuthorBooksSelector();
        }
        authorBooksSelector.setDeleteElements(forDeleteElems);
    }

    public void setSeriesBooksDeleteElements(List<String> forDeleteElems) {
        if (seriesBooksSelector == null) {
            seriesBooksSelector = new SeriesBooksSelector();
        }
        seriesBooksSelector.setDeleteElements(forDeleteElems);
    }

    private List<String> getBooksUrls(String url, List<String> deleteElements, String booksSelector) throws IOException {
        Document page = ParserHelper.getDocument(url);

        deleteElements(page, deleteElements);

        Elements books = page.select(booksSelector);

        for (Element e : books) {
            if (!e.tagName().equals("a")) {
                throw new IllegalArgumentException("Переданий селектор не силається на книжки...");
            }
        }

        return books
                .stream()
                .map(e -> e.absUrl("href"))
                .toList();
    }
    private void deleteElements(Document page, List<String> deleteElements) {
        if (deleteElements != null) {

            for (var e : deleteElements) {
                page.select(e).remove();
            }
        }
    }


    @Setter
    @Getter
    private static class AuthorBooksSelector {

        private String booksSelector;

        private List<String> matchers;

        private List<String> deleteElements;
    }

    @Setter
    @Getter
    private static class SeriesBooksSelector {

        private String booksSelector;

        private List<String> matchers;

        private List<String> deleteElements;
    }
}