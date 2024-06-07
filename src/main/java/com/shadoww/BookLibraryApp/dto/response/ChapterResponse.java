package com.shadoww.BookLibraryApp.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.shadoww.BookLibraryApp.models.Chapter;
import lombok.Value;

@Value
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChapterResponse {

    long id;

    String title;

    String text;

    long bookId;

    long chapterNumber;

    public ChapterResponse(Chapter chapter) {
        this.id = chapter.getId();
        this.title = chapter.getTitle();
        this.text = chapter.getText();
        this.bookId = chapter.getBook().getId();
        this.chapterNumber = chapter.getNumberOfPage();
    }
}
