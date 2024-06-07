package com.shadoww.BookLibraryApp.util.parser.selectors;


import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterLinks;
import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterSelectorSwitcher;
import com.shadoww.BookLibraryApp.util.parser.interfaces.ElementsFormatter;
import com.shadoww.BookLibraryApp.util.parser.interfaces.FilterElements;
import com.shadoww.BookLibraryApp.util.parser.parsers.ChapterParser;
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

    private List<String> forDeleteElements;

    private FilterElements filterElements;

    private ChapterSelectorSwitcher switcher;

    private ElementsFormatter elementsFormatter;

    public void addForDeleteElement(String element) {
        if (forDeleteElements == null) forDeleteElements = new ArrayList<>();

        forDeleteElements.add(element);
    }

    public void addForDeleteElements(String... elements) {
        if (forDeleteElements == null) forDeleteElements = new ArrayList<>();

        forDeleteElements.addAll(Arrays.stream(elements).toList());
    }
}

