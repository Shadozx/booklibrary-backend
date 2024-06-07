package com.shadoww.BookLibraryApp.dto.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ChapterRequest {

    private String title;

    private String text;

    private int numberOfPage;


    public boolean isEmpty() {
        return isTitleEmpty() && isTextEmpty() && isNumberOfPageEmpty();
    }

    public boolean isTitleEmpty() {
        return title == null || title.equals("");
    }

    public boolean isTextEmpty() {
        return text == null || text.equals("");
    }

    public boolean isNumberOfPageEmpty() {
        return numberOfPage <=0;
    }

    @Override
    public String toString() {
        return "ChapterForm{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", numberOfPage=" + numberOfPage +
                '}';
    }
}
