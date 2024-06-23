package com.shadoww.BookLibraryApp.util.parser.factories;

import com.shadoww.BookLibraryApp.model.image.ChapterImage;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import com.shadoww.BookLibraryApp.util.parser.builders.ParserBuilder;
import com.shadoww.BookLibraryApp.util.parser.interfaces.Paragraph;
import com.shadoww.BookLibraryApp.util.parser.parsers.Parser;
import com.shadoww.BookLibraryApp.util.parser.parsers.ParserHelper;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElement;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class ParserFactory {

    private ParserFactory() {
    }

    public static Parser createLibreBookParser() {

        AtomicReference<Paragraph> paragraph = new AtomicReference<>();

        paragraph.set((el, current, chapterImages, book) -> {
            Paragraph p = paragraph.get();
            if (el.tagName().equals("p")) {
                return new TextElements(List.of(ParserHelper.formatText(el, ElementType.Paragraph)));
            } else if (el.tagName().equals("div")) {

                Elements children = el.children();

                TextElements elements = new TextElements();
                if (!children.isEmpty()) {

                    for (var e : children) {
                        TextElements innerElements = p.getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);
                    }

                }
                return elements;
            } else if (el.tagName().equals("img")) {

                ChapterImage image = ParserHelper.addChapterImage(el, book);

                if (image != null) {

                    current.addChapterImage(image);

                    chapterImages.add(image);

                    TextElement element = ParserHelper.formatText(el.attr("src", image.getFilename()), ElementType.Image);

                    if (element != null) return new TextElements(List.of(element));
                }
            } else {
                TextElement element = ParserHelper.formatText(el, ElementType.Other);
                if (element != null) return new TextElements(List.of(element));
            }

            return null;
        });


        return new ParserBuilder()
                .host("librebook.me")

                .books()
                .authorBooks()
                .selector("#mangaBox > div.leftContent > div > div > div > div.desc > h3 > a")
                .matchers("\\/list\\/person\\/.+")
                .back()
                .and()

                .author()
                .nameSelector("h1.names > span.name")
                .matcher("\\/list\\/person\\/.+")
                .biographySelector("div.flex-row > div.manga-description")
                .deleteElements("noindex")
                .and()

                .bookImage()
                .selector("div > img")
                .and()

                .book()
                .matchers("\\/.+")
                .title("#mangaBox > div.leftContent > h1 > span.name")
                .description("#tab-description > div")
                .and()

                .chapters()
                .deleteElements("span.p-control", "p>span.note, br")
                .links(((bookUrl, main) -> {
                    Elements elements = main.select("#chapters-list > table > tbody a");

                    List<String> links = new ArrayList<>();

                    if (elements.isEmpty()) {
                        System.out.println("Глави відстутні!!!");
                        return List.of();
                    }


                    for (Element el : elements) {
                        links.add(el.absUrl("href"));
                    }

                    return links;
                }))
                .chapter()
                .title("#chapterSelectorSelect > span.text-cut")
                .paragraph(paragraph.get())
                .text("#mangaBox > div.b-chapter > *")
                .back()
                .and()

                .build();
    }

    public static Parser createFlibustaParser() {
        return new ParserBuilder()
                .host("flibusta.site")

                .books()
                .authorBooks()
                .matchers("\\/a\\/\\d+")
                .deleteElements("a[href^=\"/a/\"]",
                        "a[href$=\"/read\"]",
                        "a[href$=\"/download\"]",
                        "a[href$=\"/epub\"]",
                        "a[href$=\"/mobi\"]",
                        "a[href$=\"/fb2\"]",
                        "a[href$=\"/pdf\"]",
                        "a[href$=\"/html\"]",
                        "img")
                .selector("#main > form > a[href^=\"/b/\"]")
                .back()
                .seriesBooks()
                .matchers("\\/s\\/\\d+")
                .selector("#main > a[href^=\"/b/\"]")
                .deleteElements("a[href^=\"/a/\"]",
                        "a[href$=\"/read\"]",
                        "a[href$=\"/download\"]",
                        "a[href$=\"/epub\"]",
                        "a[href$=\"/mobi\"]",
                        "a[href$=\"/fb2\"]",
                        "a[href$=\"/pdf\"]",
                        "a[href$=\"/html\"]",
                        "img")
                .back()
                .and()

                .author()
                .matcher("\\/a\\/\\d+")
                .nameSelector("h1.title")
                .biographySelector("div#divabio > p")
                .deleteElements("img")
                .and()

                .series()
                .matcher("\\/s\\/\\d+")
                .nameSelector("h1.title")
                .and()

                .bookImage()
                .selector("#main > img")
                .and()

                .book()
                .matchers("\\/b\\/\\d+")
                .title("#main > h1")
                .description("#main > p")
                .and()

                .chapters()
                .links(((bookUrl, main) -> new ArrayList<>(List.of(bookUrl + "/read"))))
                .deleteElements("sup, a, form, br, ul, li, form")

                .chapter()
                .selector("#main")
                .text(".book, .poem, center img, p img")
                .title("h3.book")
                .textConvector((chapterInstances, el) -> {
                    ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                    if (el.text().matches("\\d+")) {

                        prev.addText(ParserHelper.formatText(el, ElementType.Paragraph).toPatternText());

                        chapterInstances.push(prev);

                    }

                    // якщо існує глава але має назва глави но немає тексту
                    else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

                        prev.addTitle(el.text());
                        chapterInstances.push(prev);

                    } else {
                        chapterInstances.push(prev);

                        ChapterInstance current = new ChapterInstance();

                        current.addTitle(el.text());

                        chapterInstances.push(current);

                    }
                })
                .back()

                .and()
                .build();
    }

    public static Parser createLoveReadParser() {
        AtomicReference<Paragraph> paragraph = new AtomicReference<>();

        paragraph.set((el, current, chapterImages, book) -> {
            Paragraph p = paragraph.get();
            if (el.tagName().equals("p") && el.hasClass("strong")) {

                if (!el.children().isEmpty()) {
                    return new TextElements(List.of(ParserHelper.formatText(el, ElementType.Paragraph)));
                } else {
                    Element element = el.tagName("b").removeAttr("class");

                    TextElement e = ParserHelper.formatText(element, ElementType.Other);
                    if (e != null) return new TextElements(List.of(e));

                }
            }

            if (el.tagName().equals("div")) {

                Elements children = el.children();

                TextElements elements = new TextElements();
                if (!children.isEmpty()) {

                    for (var e : children) {

                        TextElements innerElements = p.getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);


                    }

                } else {
                    el = el.tagName("p").removeAttr("class");
//                elements.add( formatText(el, ElementType.Other));
                    if (p != null) {
                        TextElements textElements = p.getParagraph(el, current, chapterImages, book);

                        if (textElements != null) elements.addAll(textElements);
                    }
                }
                return elements;
            } else if (el.tagName().equals("img")) {

                ChapterImage image = ParserHelper.addChapterImage(el, book);

                if (image != null) {

                    current.addChapterImage(image);

                    chapterImages.add(image);

                    TextElement element = ParserHelper.formatText(el.attr("src", image.getFilename()), ElementType.Image);

                    if (element != null) return new TextElements(List.of(element));
                }
            } else if (el.tagName().equals("p")) {
                TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);
                if (element != null) return new TextElements(List.of(element));
            } else {
                TextElement element = ParserHelper.formatText(el, ElementType.Other);
                if (element != null) return new TextElements(List.of(element));
            }

            return null;
        });

        return new ParserBuilder()
                .host("loveread.ec")

                .bookImage()
                .selector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(2) > td > p > a:nth-child(1) > img")
                .and()

                .books()
                .authorBooks()
                .selector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.letter_nav_bg > table > tbody > tr:nth-child(2) > td > div > div > ul> li > ul > li > a")
                .matchers("\\/biography-author\\.php\\?author=.+")
                .deleteElements("span")
                .back()
                .seriesBooks()
                .matchers("\\/series-books\\.php\\?id=\\d+")
                .selector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.letter_nav_bg > table:nth-child(1) > tbody > tr > td:nth-child(3) > p:nth-child(1) > a")
                .back()
                .and()

                .author()
                .nameSelector("h2")
                .biographySelector("div.MsoNormal")
                .deleteElements("img", "div[style]")
                .matcher("\\/biography-author\\.php\\?author=.+")
                .and()

                .series()
                .nameSelector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.letter_nav_bg > table:nth-child(1) > tbody > tr:nth-child(1) > td > div")
                .authorNameSelector("h2")
                .matcher("\\/series-books\\.php\\?id=\\d+")
                .and()

                .book()
                .matchers(".+\\/view_global\\.php\\?id=\\d+")
                .title("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(2) > td > p > strong")
                .description("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(3) > td > p.span_str")
                .and()

                .chapters()
                .links((bookUrl, main) -> {

                    bookUrl = bookUrl.contains(".me") ? bookUrl.replace(".me", ".ec") : bookUrl;

                    String urlForChapters = bookUrl.replace("view_global", "read_book") + "&p=";

                    Document firstPage = ParserHelper.getDocument(urlForChapters + 1);

                    List<Integer> numbers = firstPage.select("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.tb_read_book div.navigation a")
                            .stream()
                            .filter(a -> a.text().matches("-?\\d+(\\.\\d+)?"))
                            .map(a -> Integer.parseInt(a.text()))
                            .toList(); // .select("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.tb_read_book > div.navigation > a:nth-child(12)").text();

                    List<String> links = new ArrayList<>();

                    if (!numbers.isEmpty()) {

                        for (int n = 1; n <= numbers.get(numbers.size() - 1); n++) {
                            links.add(urlForChapters + n);
                        }

                    }
                    return links;

                })
                .filterElements(i -> !((i.tagName().equals("p") && i.parent().hasClass("em"))
                        || (i.tagName().equals("p") && !i.select("img").isEmpty())
                        || (i.tagName().equals("p") && i.parent().tagName().equals("div") && i.parent().hasClass("em"))
                        || (i.tagName().equals("img") && (i.parent().tagName().equals("form") || i.parent().tagName().equals("a")))
                        || i.tagName().equals("p") && i.attr("align").equals("center"))
                )
                .deleteElements("br", "sup", "a", "form", "div.navigation", "p[align], div:not([class])[style^=text-align], div#AdsKeeperTop")
                .switcher(elements -> elements.first().childrenSize() == 1)

                .chapter()
                .selector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.tb_read_book > div")
                .title("div.take_h1")
                .text(">div, >p, img")
                .paragraph(paragraph.get())
                .back()

                .chapter()
                .selector("div.MsoNormal")
                .title("div.take_h1")
                .text(">div, >p, img")
                .paragraph(paragraph.get())
                .back()
                .and()

                .build();
    }

    public static Parser createCoolLibParser() {

        AtomicReference<Paragraph> paragraph = new AtomicReference<>();

        paragraph.set((element, current, chapterImages, book) -> {

            if (element.tagName().equals("p")) {

                Elements elements = element.select("img");

                if (elements.isEmpty()) {
                    TextElement el = ParserHelper.formatText(element, ElementType.Paragraph);
                    if (el != null && !element.html().trim().isEmpty()) return new TextElements(List.of(el));
                } else {
                    TextElements textElements = new TextElements();
                    for (Element el : elements) {
                        TextElements innerElements = paragraph.get().getParagraph(el, current, chapterImages, book);
                        if (innerElements != null) textElements.addAll(innerElements);
                    }

                    return textElements;
                }
            } else if (element.hasClass("book")) {
                TextElement el = ParserHelper.formatText(element, ElementType.Paragraph);

                if (el != null) return new TextElements(List.of(el));

            } else if (element.tagName().equals("div") || element.hasClass("epigraph")) {


                Elements children = element.children();

                TextElements elements = new TextElements();


                if (!element.ownText().equals("") && !element.children().isEmpty()) {

                    Element newEl = new Element(element.tagName()).text(element.ownText());

                    elements.add(ParserHelper.formatText(newEl, ElementType.Paragraph));
                }

                if (!children.isEmpty()) {

                    for (var e : children) {
                        TextElements innerElements = paragraph.get().getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);

                    }

                } else {
                    element = element.tagName("p").removeAttr("class");
//                elements.add( formatText(el, ElementType.Other));
                    TextElements els = paragraph.get().getParagraph(element, current, chapterImages, book);
                    if (els != null) elements.addAll(els);
                }
                return elements;
            } else if (element.tagName().equals("img")) {

                ChapterImage image = ParserHelper.addChapterImage(element, book);

                if (image != null) {

                    current.addChapterImage(image);

                    chapterImages.add(image);

                    TextElement el = ParserHelper.formatText(element.attr("src", image.getFilename()), ElementType.Image);

                    if (el != null) return new TextElements(List.of(el));
                }
            } else {
                TextElement el = ParserHelper.formatText(element, ElementType.Other);
                if (element != null) return new TextElements(List.of(el));
            }

            return null;
        });

        return new ParserBuilder()
                .host("coollib.net")

                .books()
                .authorBooks()
                .selector("#abooks > div.boline > a:nth-child(3)")
                .matchers("\\/a\\/.+")
                .back()
                .seriesBooks()
                .selector("#abooks > div.boline > a:nth-child(3)")
                .matchers("\\/s\\/\\d+")
                .back()
                .and()

                .author()
                .matcher("\\/a\\/.+")
                .nameSelector("div#content h1")
                .and()

                .series()
                .matcher("\\/s\\/\\d+")
                .nameSelector("div#content h1")
                .and()

                .bookImage()
                .selector("#bbookk picture > img")
                .and()

                .book()
//                .title("#postconn > h1")
                .matchers("\\/b\\/.+")
                .title("div > h1")
//                .description("#bbookk > table > tbody > tr > td:nth-child(2) > p")
                .description("#ann p")
                .and()

                .chapters()
                .deleteElements("ul", "li")
                .links(((bookUrl, main) -> new ArrayList<>(List.of(bookUrl + "/read"))))
                .format((elements -> {

                    if (elements.size() == 1) {
                        Stack<Element> newElements = new Stack<>();

                        Element main = elements.first();


                        if (main != null) {

                            List<Node> nodes = main.childNodes();

                            Element current = new Element("p");

                            List<String> tags = List.of("h1", "h2", "h3", "h4", "h5", "h6",
                                    "p",
                                    "img",
                                    "div");

                            if (nodes.stream().anyMatch(node -> node instanceof TextNode)) {

                                for (var node : nodes) {

                                    if (node instanceof Element) {
                                        Element e = (Element) node;

                                        //                        all_tags.add(e.tagName() + (e.className().equals("") ? "" : "." + e.className()));
                                    }
                                    if (node != null) {
                                        if (node instanceof Element) {

                                            Element e = (Element) node;


                                            if (e.hasClass("book") || e.hasClass("epigraph") || tags.contains(e.tagName())) {
                                                newElements.push(current);
                                                newElements.push(e);

                                                current = new Element("p");

                                            } else {
                                                current.append(e.toString());
                                            }

                                        } else if (node instanceof TextNode) {
                                            TextNode textNode = (TextNode) node;

                                            current.append(textNode.text());
                                        }

                                    }
                                }
                            }
                        } else {
                            newElements.addAll(main.children().stream().toList());
                        }

                        return new Elements(newElements);
                    } else return elements;
                }))
                .deleteElements("br", "sup", "a", "script", "ins", "small", "ul", "li")

                .chapter()
                .textConvector((chapterInstances, el) -> {
                    ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                    if (el.text().matches("\\d+")) {

                        prev.addText(ParserHelper.formatText(el, ElementType.Paragraph).toPatternText());

                        chapterInstances.push(prev);

                    }

                    // якщо існує глава але має назва глави но немає тексту
                    else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

                        prev.addTitle(el.text());
                        chapterInstances.push(prev);

                    } else {
                        chapterInstances.push(prev);

                        ChapterInstance current = new ChapterInstance();

                        current.addTitle(el.text());

                        chapterInstances.push(current);

                    }
                })
                .title("h3.book")
                .title("h3")
                .selector("#frd")
                .paragraph(paragraph.get())
                .back()

                .and()
                .build();
    }

    public static Parser createRulitParser() {

        AtomicReference<Paragraph> paragraph = new AtomicReference<>();

        paragraph.set((el, current, chapterImages, book) -> {
            if (el.tagName().equals("div")) {

                Elements children = el.children();

                TextElements elements = new TextElements();
                if (!children.isEmpty()) {

                    for (var e : children) {
                        TextElements innerElements = paragraph.get().getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);
                    }

                } else {
                    el = el.tagName("p").removeAttr("class");
//                elements.add( formatText(el, ElementType.Other));

                    TextElements textElements = paragraph.get().getParagraph(el, current, chapterImages, book);

                    if (textElements != null) elements.addAll(textElements);

                }
                return elements;
            } else if (el.tagName().equals("img")) {

                ChapterImage image = ParserHelper.addChapterImage(el, book);

                if (image != null) {

                    current.addChapterImage(image);

                    chapterImages.add(image);

                    TextElement element = ParserHelper.formatText(el.attr("src", image.getFilename()), ElementType.Image);

                    if (element != null) return new TextElements(List.of(element));
                }
            } else if (el.tagName().equals("p")) {
                TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);
                if (element != null) return new TextElements(List.of(element));
            } else {
                TextElement element = ParserHelper.formatText(el, ElementType.Other);
                if (element != null) return new TextElements(List.of(element));
            }

            return null;
        });

        return new ParserBuilder()
                .host("rulit.me")

                .books()
                .authorBooks()
                .matchers("\\/author\\/.+")
                .selector("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article > div > div.media-body > div > div.entry-header.text-left.text-uppercase > h4 > a")
                .back()
                .seriesBooks()
                .matchers("\\/series\\/.+")
                .selector("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article > div > div.media-body > div > div.entry-header.text-left.text-uppercase > h4 > a")
                .back()
                .and()

                .bookImage()
                .selector("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9  div.post-thumb.col-sm-6.text-center > img")
                .and()

                .book()
                .matchers("\\/books\\/.+", "\\/series\\/.+", "\\/author\\/.+")
                .title("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > h2")
                .description("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article:nth-child(2) > div:nth-child(2) > div > div > p")
                .and()
                .chapters()
                .links((bookUrl, main) -> {
//                    main.select("div.book_info").remove();
//                    Element firstPage = main.select("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article:nth-child(2) > div:nth-child(1) > div.post-content.col-sm-6 > div:nth-child(2) > a:not([rel])").first();

//                    System.out.println(firstPage);

//                    if (firstPage != null) {

                    System.out.println(bookUrl);


//                        String first = firstPage.absUrl("href");
//
//                        System.out.println("First:" + first);
//                        String urlForParse = first.replace("1.html", "");

                    String readUrl = bookUrl.replace("download", "read").replace(".html", "");
                    System.out.println(readUrl);


                    String lastLink = readUrl + "-10000.html";

                    System.out.println(lastLink);
                    Document lastPage = ParserHelper.getDocument(lastLink);

                    System.out.println(lastPage.location());

                    Element elementAmount = lastPage.select("ul.pagination > li.active a").first();

                    if (elementAmount != null) {
                        int amount = Integer.parseInt(elementAmount.text());

                        System.out.println(amount);
                        return IntStream.range(1, amount + 1).mapToObj(i -> readUrl + "-" + i + ".html").toList();
                    } else {
                        System.out.println("Не вийшло отримати кількість сторінок");
                    }

//                    }
                    return null;
                })
                .deleteElements(".hidden-xs", "div.empty-line", "span.title, a, sup, .note_section, .page_divided_line")

                .chapter()
                .title("div.title")
                .selector("body > div.boxed.container > div > div:nth-child(2) > div > div.row.page-content-row > div > article>*")
                .paragraph(paragraph.get())
                .back()
                .and()

                .build();
    }

    public static Parser createMiliteraParser() {
        AtomicReference<Paragraph> paragraph = new AtomicReference<>();

        paragraph.set((el, current, chapterImages, book) -> {
            if (el.tagName().equals("p")) {

                TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);

                if (element != null) {

                    return new TextElements(List.of(element));
                }

            } else if (el.tagName().equals("div")) {
                Elements children = el.children();

                TextElements elements = new TextElements();
                if (!children.isEmpty()) {

                    for (var e : children) {
                        TextElements innerElements = paragraph.get().getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);
                    }

                } else {
                    el = el.tagName("p").removeAttr("class");
//                elements.add( formatText(el, ElementType.Other));
                    TextElements textElements = paragraph.get().getParagraph(el, current, chapterImages, book);

                    if (textElements != null) elements.addAll(textElements);
                }
                return elements;
            } else if (el.tagName().equals("img")) {

                return paragraph.get().getParagraph(el, current, chapterImages, book);
            } else {
                if (el.childrenSize() != 0) {
                    Elements children = el.children();

                    TextElements elements = new TextElements();
                    for (Element e : children) {

                        TextElements textElements = paragraph.get().getParagraph(e, current, chapterImages, book);

                        if (textElements != null) {
                            elements.addAll(textElements);
                        }

                    }

                    return elements;

                } else {
                    TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);

                    if (element != null) {
                        return new TextElements(List.of(element));
                    }
                }
            }

            return null;
        });

        AtomicReference<Paragraph> paragraph2 = new AtomicReference<>();

        paragraph2.set((el, current, chapterImages, book) -> {
            if (el.hasClass("book")) {

                if (!el.tagName().equals("p")) {

                    Elements children = el.children();

                    TextElements elements = new TextElements();
                    if (!children.isEmpty()) {

                        for (var e : children) {
                            TextElements innerElements = paragraph2.get().getParagraph(e, current, chapterImages, book);
                            if (innerElements != null) elements.addAll(innerElements);

                        }
                        return elements;
                    } else return null;
                } else {
                    TextElement e = ParserHelper.formatText(el, ElementType.Paragraph);
                    if (e != null) return new TextElements(List.of(e));
                }
            } else if (el.hasClass("epigraph") || el.tagName().equals("div")) {

                Elements children = el.children();

                TextElements elements = new TextElements();
                if (!children.isEmpty()) {

                    for (var e : children) {
                        TextElements innerElements = paragraph2.get().getParagraph(e, current, chapterImages, book);
                        if (innerElements != null) elements.addAll(innerElements);
                    }

                } else {
//                elements.add( formatText(el, ElementType.Other));
                    TextElement element = ParserHelper.formatText(el, ElementType.Paragraph);

                    if (element != null) elements.add(element);
                }
                return elements;
            } else if (el.tagName().equals("img")) {

                return ParserHelper.getParagraphImage(el, current, book, chapterImages);
            } else {
                TextElement element = ParserHelper.formatText(el, ElementType.Other);
                if (element != null) return new TextElements(List.of(element));
            }

            return null;
        });

        return new ParserBuilder()
                .host("militera.lib")

                .book()
                .matchers("\\/.+\\/.+\\/index\\.html")
                .title("div.tname")
                .description("#annot")
                .and()

                .chapters()
                .deleteElements("a, sup, br, script, li, small")

                .chapter()
                .links((bookUrl, main) -> {
                    Element content = main.select("div.cont").first();

                    if (content != null) {
                        Elements pages = content.select("a");

                        List<String> urlpages = pages.stream().map(el -> el.absUrl("href")).distinct().toList();

                        return urlpages;
                    }

                    return null;
                })
                .selector("div.b>*")
                .title("h3")
                .paragraph(paragraph.get())
                .back()

                .chapter()
                .links((bookUrl, main) -> {
                    Element nfor = main.select("#nfor").first();

                    if (nfor != null) {
                        Element html = nfor.select("a:matchesOwn(html)").first();
                        System.out.println(html);

                        if (html != null) {
                            String book = html.absUrl("href");

                            return new ArrayList<>(List.of(book));
                        }
                    }
                    return null;
                })
                .title("h3.book")
                .selector("body>*")
                .textConvector((chapterInstances, el) -> {
                    ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                    if (el.text().matches("\\d+")) {

                        prev.addText(ParserHelper.formatText(el, ElementType.Paragraph).toPatternText());

                        chapterInstances.push(prev);

                    }

                    // якщо існує глава але має назва глави но немає тексту
                    else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

                        prev.addTitle(el.text());
                        chapterInstances.push(prev);

                    } else {
                        chapterInstances.push(prev);

                        ChapterInstance current = new ChapterInstance();

                        current.addTitle(el.text());

                        chapterInstances.push(current);

                    }
                })
                .paragraph(paragraph2.get())
                .back()

                .and()
                .build();
    }

    public static Parser create4italkaParser() {

        return new ParserBuilder()
                .host("4italka.su")

                .books()
                .authorBooks()
                .matchers("\\/author\\/.+\\.htm")
                .selector("#page > main > div.author-books > div > div > div.short-recent-items.extended.boxes-6.figure-m-v-10 > div> div.desc-content > h4 > a")
                .back()
                .and()

                .book()
                .matchers("\\/\\w+\\/\\w+\\/\\d+\\/fulltext\\.htm", "\\/\\w+\\/\\w+\\/\\d+\\.htm")
                .title("#page > main > div.about-book > div.about-book__desc > div.about-book__desc-info > h1")
                .description("#page > main > div.about-book > div.about-book__desc > div.about-book__desc-info > div.about-book__desc-info-txt > p")
                .and()

                .chapters()
                .links(((bookUrl, main) -> List.of(bookUrl.contains("fulltext.htm") ? bookUrl : bookUrl.replace(".htm", "fulltext.htm"))))
                .chapter()
                .title("h3")
                .selector("#book > div > div > div.toread-text.m-v-30 > div.text-content.box-md-8.m-auto > *")
                .textConvector((chapterInstances, el) -> {
                    ChapterInstance prev = !chapterInstances.isEmpty() ? chapterInstances.pop() : new ChapterInstance();
                    if (el.text().matches("\\d+")) {

                        prev.addText(ParserHelper.formatText(el, ElementType.Paragraph).toPatternText());

                        chapterInstances.push(prev);

                    }

                    // якщо існує глава але має назва глави но немає тексту
                    else if (!prev.isTitleEmpty() && prev.isTextEmpty()) {

                        prev.addTitle(el.text());
                        chapterInstances.push(prev);

                    } else {
                        chapterInstances.push(prev);

                        ChapterInstance current = new ChapterInstance();

                        current.addTitle(el.text());

                        chapterInstances.push(current);

                    }
                })
                .back()
                .and()

                .build();
    }

    public static Parser createParserForHost(String host) {
        return switch (host) {
            case "loveread.ec" -> createLoveReadParser();
            case "coollib.in", "coollib.xyz", "coollib.net" -> createCoolLibParser();
            case "librebook.me" -> createLibreBookParser();
            case "rulit.me", "www.rulit.me" -> createRulitParser();
            case "flibusta.site" -> createFlibustaParser();
            case "militera.lib" -> createMiliteraParser();
            case "4italka.su" -> create4italkaParser();
            default -> throw new IllegalArgumentException("Цей сайт не підтримується");
        };
//        return parsers.stream().filter(p -> Objects.equals(p.getHost(), host)).findFirst().orElse(null);
    }
}