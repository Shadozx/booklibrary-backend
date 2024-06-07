package com.shadoww.BookLibraryApp.util.parser.selectors;

import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterLinks;
import com.shadoww.BookLibraryApp.util.parser.interfaces.FilterElements;
import com.shadoww.BookLibraryApp.util.parser.interfaces.Paragraph;
import com.shadoww.BookLibraryApp.util.parser.interfaces.TextConvector;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ChapterSelector {

    private List<String> titles;

    private String textSelector;

    private String selector;

    private List<String> deleteElements;

    private TextConvector textConvector;

    // звідки брати силки на глави
    private ChapterLinks chapterLinks;


    // як перетворювати елементи тексту
    private Paragraph paragraph;


    private FilterElements filterElements;


    public void addTitle(String title) {
        if (this.titles == null) this.titles = new ArrayList<>();

        this.titles.add(title);
    }
}

