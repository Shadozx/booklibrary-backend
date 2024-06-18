package com.shadoww.BookLibraryApp.dto.response;

import com.shadoww.BookLibraryApp.model.BookSeries;
import lombok.Value;

@Value
public class BookSeriesResponse {

    long id;

    String title;

    String description;

    String uploadedUrl;

    public BookSeriesResponse(BookSeries bookSeries) {
        this.id = bookSeries.getId();
        this.title = bookSeries.getTitle();
        this.description = bookSeries.getDescription();
        this.uploadedUrl = bookSeries.getUploadedUrl();
    }
}
