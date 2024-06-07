package com.shadoww.BookLibraryApp.util.instances;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BookInstance {

    private String title;

    private String description;


    private int amount;

    private List<ChapterInstance> chapters;


    public BookInstance(String parsedTitle, String parsedDescription) {
        this.title = parsedTitle;
        this.description = parsedDescription;

        chapters = new ArrayList<>();
    }

    public int getAmount() {
        return chapters.size();
    }

    @Override
    public String toString() {
        return "BookInstance{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + getAmount() +
                '}';
    }
}

