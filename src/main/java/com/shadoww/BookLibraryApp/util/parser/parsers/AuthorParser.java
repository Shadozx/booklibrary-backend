package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.model.Author;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

@Setter
public class AuthorParser {

    private AuthorInformationSelector authorInformationSelector;

    @Getter
    private String matcher;

    private List<String> deleteElements;

    public boolean canParseAuthor(String authorUrl) {

        return matcher != null && authorUrl.matches(".+" + matcher);
    }

    public Author parseAuthor(String authorUrl) throws IOException {
        Document authorPage = ParserHelper.getDocument(authorUrl);

        if (deleteElements != null) {
            for (var e : deleteElements) {
                authorPage.select(e).remove();
            }
        }

        Element name = authorPage.select(authorInformationSelector.getNameSelector()).first();
        Element biography = authorPage.select(authorInformationSelector.getBiographySelector()).first();

        if (name == null) {
            throw new IllegalArgumentException("Автор не може бути без імені!");
        }

        Author author = new Author();

        author.setName(name.text());

        if (biography != null) {
            author.setBiography(biography.text());
        }

        return author;

    }

    public void setNameSelector(String nameSelector) {
        if (authorInformationSelector == null) {
            authorInformationSelector = new AuthorInformationSelector();
        }

        authorInformationSelector.setNameSelector(nameSelector);
    }


    public void setBiographySelector(String biographySelector) {
        if (authorInformationSelector == null) {
            authorInformationSelector = new AuthorInformationSelector();
        }

        authorInformationSelector.setBiographySelector(biographySelector);
    }


    @Setter
    @Getter
    private static class AuthorInformationSelector {
        private String nameSelector;
        private String biographySelector;
    }
}