package com.shadoww.BookLibraryApp.util.parser.selectors;


import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterLinks;
import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterSelectorSwitcher;
import com.shadoww.BookLibraryApp.util.parser.interfaces.ElementsFormatter;
import com.shadoww.BookLibraryApp.util.parser.interfaces.FilterElements;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

@Setter
@Getter
public class ChapterSelectors extends Stack<ChapterSelector> {

    // Звідки брати силки на глави
    private ChapterLinks chapterLinks;

    private List<String> deleteElements;

    private FilterElements filterElements;

    private ChapterSelectorSwitcher switcher;

    private ElementsFormatter elementsFormatter;

    public void addDeleteElement(String element) {
        if (deleteElements == null) {
            deleteElements = new ArrayList<>();
        }

        deleteElements.add(element);
    }

    public void addDeleteElements(String... elements) {
        if (deleteElements == null) {
            deleteElements = new ArrayList<>();
        }

        deleteElements.addAll(Arrays.asList(elements));
    }
}

