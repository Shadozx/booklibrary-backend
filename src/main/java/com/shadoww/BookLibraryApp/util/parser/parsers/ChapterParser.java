package com.shadoww.BookLibraryApp.util.parser.parsers;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import com.shadoww.BookLibraryApp.util.parser.interfaces.*;
import com.shadoww.BookLibraryApp.util.parser.selectors.ChapterSelector;
import com.shadoww.BookLibraryApp.util.parser.selectors.ChapterSelectors;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElement;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class ChapterParser {

    @Setter
    private Book book;

    @Setter
    private ChapterSelectors chapterSelectors;


    private List<Chapter> chapters;


    @Getter
    private final List<ChapterImage> chapterImages = new ArrayList<>();


    private String bookUrl;




    public List<Chapter> parse(String bookUrl, Book book) throws IOException {

        this.book = book;
        this.bookUrl = bookUrl;

        List<String> links = getLinks(chapterSelectors);

        if (links != null && !links.isEmpty()) {

            Stack<ChapterInstance> chapterInstances = new Stack<>();
            for (String link : links) {

                Document page = ParserHelper.getDocument(link);

                if (chapterSelectors != null) {

                    // видаляємо не потрібні елементи
                    if (chapterSelectors.getForDeleteElements() != null) page.select(String.join(", ", chapterSelectors.getForDeleteElements())).remove();


                    Stack<ChapterInstance> parsedChapterInstances = getChapterInstances(chapterSelectors, page);

                    if (parsedChapterInstances != null) {
                        chapterInstances.addAll(parsedChapterInstances);
                    }
                }
            }

            List<ChapterInstance> formatChapters = formatChapters(chapterInstances);

            this.chapters = addNumber(formatChapters.stream().filter(c->!c.isTextEmpty()).map(Chapter::new).toList());

            return this.chapters;

        }

        return null;
    }


    /**
     * книжка:
     *      селектор глави
     *      селектор опису
     *
     * фотографія книжки:
     *      селектор на фотографію
     * <p>
     * глави:
     *      глава:
     *          селектор на назву глави
     *          селектор на текст
     * <p>
     *
     * пишемо функцію яка дає силки на глави
     * <p>
     *
     *
     * */

    //
    private List<String> getLinks(ChapterSelectors chapterSelectors) throws IOException {

        if (!chapterSelectors.isEmpty()) {
            for(ChapterSelector chapterSelector : chapterSelectors) {

                ChapterLinks chapterLinks = chapterSelector.getChapterLinks();

                if (chapterLinks == null) {
                    chapterLinks = chapterSelectors.getChapterLinks();

                }

                List<String> links = chapterLinks.getChapterLinks(bookUrl, ParserHelper.getDocument(bookUrl));

                if (links != null) return links;
            }


        }
        return null;
    }


    private Stack<ChapterInstance> getChapterInstances(ChapterSelectors chapterSelectors, Document page) {

        for(ChapterSelector chapterSelector : chapterSelectors) {

            Stack<ChapterInstance> chapterInstances = getPage(chapterSelector, page);

            if (chapterInstances != null) return chapterInstances;

        }

        return null;

    }


    private Stack<ChapterInstance> getPage(ChapterSelector chapterSelector, Document page) {
        String selector = chapterSelector.getSelector();

        if (selector != null) {
            Elements elements = page.select(selector);


            ChapterSelectorSwitcher switcher = chapterSelectors.getSwitcher();

            if (switcher != null) {
                if (switcher.switchSelector(elements)) return null;
            }

            ElementsFormatter elementsFormatter = chapterSelectors.getElementsFormatter();

            if (elementsFormatter != null) elements = elementsFormatter.format(elements);

            if (chapterSelector.getTextSelector() != null && !chapterSelector.getTextSelector().equals("")) elements = elements.select(chapterSelector.getTextSelector());


            FilterElements filterElements = chapterSelectors.getFilterElements();



            if (filterElements != null)
                elements = elements.stream().filter(filterElements::filter).collect(Collectors.toCollection(Elements::new));


            if (!elements.isEmpty()) {

//                System.out.println(elements.size());

                Stack<ChapterInstance> chapterInstances = new Stack<>();


                for (Element el : elements) {


                    if (chapterSelector.getTitles().contains(el.tagName() + (!el.className().equals("") ? ("." + el.className()) : ""))) {

                        TextConvector textConvector = chapterSelector.getTextConvector();
                        if (textConvector != null) textConvector.transform(chapterInstances, el);

                        else {
                            ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();


                            addTitle(el, prev, chapterInstances);
                        }
                    } else {

                        if (chapterSelector.getParagraph() != null) {
                            ChapterInstance current = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                            TextElements textElements = chapterSelector.getParagraph().getParagraph(el, current, this.chapterImages, this.book);

                            if (textElements != null) current.addText(textElements.toPatternText());

                            chapterInstances.push(current);
                        }
                        else {
//                            System.out.println("Paragraph is empty...");

                            ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();

                            TextElements textElements = getParagraphText(el, prev);

                            if (textElements != null) prev.addText(textElements.toPatternText());

                            chapterInstances.push(prev);
                        }
                    }
                }

                return chapterInstances;
            }
            else System.out.println("Елементи тексту відсутні");
        }
        else {
            List<String> titles = chapterSelector.getTitles();

            if (titles.size() == 1) {
                String selectorTitle = titles.get(0);
                Element elementTitle = page.select(selectorTitle).first();

                ChapterInstance current = new ChapterInstance();
                current.addTitle(elementTitle != null ? elementTitle.text() : "* * *");

                Elements elements = page.select(chapterSelector.getTextSelector());

                if (elements != null) {

                    for (Element el : elements) {
                        Paragraph paragraph = chapterSelector.getParagraph();


                        TextElements textElements;
                        if (paragraph != null) {
                             textElements = paragraph.getParagraph(el, current, this.chapterImages, this.book);

                        }else {
                             textElements = getParagraphText(el, current);
                        }
                        if (textElements != null) current.addText(textElements.toPatternText());
                    }
                }

                Stack<ChapterInstance> chapterInstances = new Stack<>();

                chapterInstances.push(current);
                return chapterInstances;
            }


        }
        return null;
    }


    private void addTitle(Element el, ChapterInstance prev, Stack<ChapterInstance> chapterInstances) {

        if(prev.getTitle().equals("Примечания") && el.text().matches("\\d+")) {

            prev.addText(ParserHelper.formatText(el, ElementType.Paragraph).toPatternText());

            chapterInstances.push(prev);
        }


        // якщо існує глава, але має назву глави но немає тексту
        else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

            prev.addTitle(el.text());
            chapterInstances.push(prev);

        }


        else {
            chapterInstances.push(prev);

            ChapterInstance current = new ChapterInstance();

            current.addTitle(el.text());

            chapterInstances.push(current);

        }

    }
    private TextElements getParagraphText(Element el, ChapterInstance current) {
        if (el.tagName().equals("img")) {
            ChapterImage image = ParserHelper.addChapterImage(el, this.book);

            if (image != null) {
                current.addChapterImage(image);

                this.chapterImages.add(image);

                TextElement element = ParserHelper.formatText(el.attr("src", image.getFilename()), ElementType.Image);

                if (element != null) return new TextElements(List.of(element));
            }
        } else {
            TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);

            if (element != null) return new TextElements(List.of(element));


        }

        return null;
    }
    private List<ChapterInstance> formatChapters(List<ChapterInstance> chapters) {
        Stack<ChapterInstance> stack = new Stack<>();
        chapters = chapters.stream().filter(c-> !c.isEmpty()).toList();

        for(int i = 0; i < chapters.size(); i++) {
            ChapterInstance current = chapters.get(i);

            if(current != null) {
                if(i == 0 && current.isTitleEmpty()) {

                    current.addTitle("* * *");
                    stack.push(current);
                }
//                else if(current.isTextEmpty()) {
//                    ChapterInstance next = i < chapters.size() ? chapters.get(i+1) : null;
//                    if(next != null) {
//                        next.addPreTitle(current.getTitle());
//                    }
//
//                }

                else if(current.isTitleEmpty()) {
                    ChapterInstance prev = !stack.isEmpty() ? stack.pop() : new ChapterInstance();

                    prev.addChapterInstance(current);
                    stack.push(prev);
                }
                else stack.push(current);
            }
        }

        return stack;
    }


    private List<Chapter> addNumber(List<Chapter> chapters) {
        for(int i = 1; i <= chapters.size(); i++) chapters.get(i-1).setNumberOfPage(i);

        return chapters;
    }


}

/**
 *
 * Парсер:
 *  селектор на кількість глав якщо такий є
 * <p>
 *  список глав селекторів який буде йти по списку якщо один не дав хорошого результату
 *      Глав селектор:
 * <p>
 *
 *
 *
 *
 *
 * */