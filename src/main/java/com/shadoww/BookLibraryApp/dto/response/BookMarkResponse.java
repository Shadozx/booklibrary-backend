package com.shadoww.BookLibraryApp.dto.response;

import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.Chapter;
import lombok.Value;

@Value
public class BookMarkResponse {

    long id;

    long paragraph;

    long catalogId;

    long bookId;
    long chapterId;
    long ownerId;

    public BookMarkResponse(BookMark mark) {
        this.id = mark.getId();
        this.paragraph = mark.getParagraph();
        this.bookId = mark.getBook().getId();
        this.catalogId = mark.getCatalog().getId();
        this.ownerId = mark.getOwner().getId();

        Chapter chapter = mark.getChapter();

        if (chapter != null) {
            this.chapterId = chapter.getId();

        }else {
            this.chapterId = 0;
        }
    }
}
