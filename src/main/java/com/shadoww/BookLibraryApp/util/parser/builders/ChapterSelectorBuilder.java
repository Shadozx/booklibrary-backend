package com.shadoww.BookLibraryApp.util.parser.builders;


import com.shadoww.BookLibraryApp.util.parser.interfaces.ChapterLinks;
import com.shadoww.BookLibraryApp.util.parser.interfaces.Paragraph;
import com.shadoww.BookLibraryApp.util.parser.interfaces.TextConvector;
import com.shadoww.BookLibraryApp.util.parser.selectors.ChapterSelector;

public class ChapterSelectorBuilder {

    private ChapterParserBuilder chapterParserBuilder;

    private ChapterSelector chapterSelector;


    public ChapterSelectorBuilder(ChapterParserBuilder chapterParserBuilder) {
        this.chapterSelector = new ChapterSelector();

        this.chapterParserBuilder = chapterParserBuilder;
    }

    public ChapterSelectorBuilder links(ChapterLinks chapterLinks) {

        this.chapterSelector.setChapterLinks(chapterLinks);


        return this;
    }

    public ChapterSelectorBuilder title(String title) {

        chapterSelector.addTitle(title);


        return this;
    }

    public ChapterSelectorBuilder text(String text) {

        chapterSelector.setTextSelector(text);


        return this;
    }

    public ChapterSelectorBuilder textConvector(TextConvector textConvector) {

        this.chapterSelector.setTextConvector(textConvector);

        return this;
    }


    public ChapterSelectorBuilder paragraph(Paragraph paragraph) {
        this.chapterSelector.setParagraph(paragraph);

        return this;
    }

    public ChapterSelectorBuilder selector(String selector) {
        chapterSelector.setSelector(selector);

        return this;
    }


    public ChapterParserBuilder back() {

        this.chapterParserBuilder.getChapterSelectors().add(this.chapterSelector);

        return this.chapterParserBuilder;
    }
}
