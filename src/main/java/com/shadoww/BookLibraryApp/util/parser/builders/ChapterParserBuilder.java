package com.shadoww.BookLibraryApp.util.parser.builders;

import com.shadoww.BookLibraryApp.util.parser.interfaces.*;
import com.shadoww.BookLibraryApp.util.parser.parsers.ChapterParser;
import com.shadoww.BookLibraryApp.util.parser.selectors.ChapterSelector;
import com.shadoww.BookLibraryApp.util.parser.selectors.ChapterSelectors;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChapterParserBuilder {

    private ParserBuilder parserBuilder;

    private ChapterParser chapterParser;


    private ChapterSelectors chapterSelectors;

    private FilterElements filterElements;

    private List<String> fromBookChapters;

    public ChapterParserBuilder(ParserBuilder parserBuilder) {
        this.parserBuilder = parserBuilder;
        this.chapterParser = new ChapterParser();
        this.chapterSelectors = new ChapterSelectors();
    }

    public ChapterParserBuilder links(ChapterLinks chapterLinks) {

        this.chapterSelectors.setChapterLinks(chapterLinks);

        return this;
    }

    public ChapterParserBuilder filterElements(FilterElements filterElements) {
        this.chapterSelectors.setFilterElements(filterElements);

        return this;
    }

    public ChapterParserBuilder switcher(ChapterSelectorSwitcher switcher) {
        this.chapterSelectors.setSwitcher(switcher);

        return this;
    }

    public ChapterParserBuilder format(ElementsFormatter elementsFormatter) {
        this.chapterSelectors.setElementsFormatter(elementsFormatter);

        return this;
    }

    public ChapterParserBuilder deleteElements(String... elements) {
        this.chapterSelectors.addForDeleteElements(elements);

        return this;
    }


    public ChapterSelectorBuilder chapter() {
        return new ChapterSelectorBuilder(this);
    }


    public ParserBuilder and() {
        this.chapterParser.setChapterSelectors(this.chapterSelectors);
        return this.parserBuilder;
    }
}
