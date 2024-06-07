package com.shadoww.BookLibraryApp.util.parser.factories;

import com.shadoww.BookLibraryApp.models.images.ChapterImage;
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
                .books()
                .author("#mangaBox > div.leftContent > div > div > div > div.desc > h3 > a")
                .and()
                .bookImage()
                .selector("div > img")
                .and()
                .book()
                .title("#mangaBox > div.leftContent > h1 > span.name")
                .description("#tab-description > div")
                .and()
                .chapters()
                .deleteElements("span.p-control", "p>span.note, br")
                .links(((bookUrl, main) -> {
                    Elements elements = main.select("#chapters-list > table > tbody a");

                    List<String> links = new ArrayList<>();

                    if (elements.isEmpty()) {
                        throw new RuntimeException("Глави відстутні!!!");
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
                .books()
                .deleteElementsAuth("a[href^=\"/a/\"]",
                        "a[href$=\"/read\"]",
                        "a[href$=\"/download\"]",
                        "a[href$=\"/epub\"]",
                        "a[href$=\"/mobi\"]",
                        "a[href$=\"/fb2\"]",
                        "a[href$=\"/pdf\"]",
                        "a[href$=\"/html\"]")
                .author("#main > form > a[href^=\"/b/\"]")
                .and()
                .bookImage()
                .selector("#main > img")
                .and()
                .book()
                .title("#main > h1")
                .description("#main > p")
                .and()
                .chapters()
                .links(((bookUrl, main) -> {


                    List<String> links = new ArrayList<>();

                    links.add(bookUrl + "/read");


                    return links;
                }))
                .deleteElements("sup, a, form, br, ul, li, form")
                .chapter()
                .selector("#main")
                .text(".book, .poem, center img")
                .title("h3.book")
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
                .bookImage()
                .selector("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(2) > td > p > a:nth-child(1) > img")
                .and()
                .books()
                .author("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.letter_nav_bg > table:gt(0) > tbody td p a")
                .series("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.letter_nav_bg > table:nth-child(1) > tbody > tr > td:nth-child(2) > a")
                .and()
                .book()
                .title("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(2) > td > p > strong")
                .description("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td:nth-child(2) > table > tbody > tr > td > table > tbody > tr:nth-child(3) > td > p.span_str")
                .and()
                .chapters()
                .links((bookUrl, main) -> {

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
                .books()
                .author("#abooks > div.boline > a:nth-child(3)")
                .and()
                .bookImage()
                .selector("#bbookk picture > img")
                .and()
                .book()
//                .title("#postconn > h1")
                .title("div > h1")
//                .description("#bbookk > table > tbody > tr > td:nth-child(2) > p")
                .description("#ann p")
                .and()
                .chapters()
                .deleteElements("ul", "li")
                .links(((bookUrl, main) -> {
                    List<String> links = new ArrayList<>();
                    links.add(bookUrl + "/read");

                    return links;
                }))
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
                .books()
                .author("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article > div > div.media-body > div > div.entry-header.text-left.text-uppercase > h4 > a")
                .and()
                .bookImage()
                .selector("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9  div.post-thumb.col-sm-6.text-center > img")
                .and()
                .book()
                .title("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > h2")
                .description("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article:nth-child(2) > div:nth-child(2) > div > div > p")
                .and()
                .chapters()
                .links((bookUrl, main) -> {
                    main.select("div.book_info").remove();
                    Element firstPage = main.select("body > div.boxed.container > div > div:nth-child(2) > div.col-lg-9 > div > div > article:nth-child(2) > div:nth-child(1) > div.post-content.col-sm-6 > div:nth-child(2) > a:not([rel])").first();

                    System.out.println(firstPage);
                    if (firstPage != null) {


                        String first = firstPage.absUrl("href");

                        System.out.println("First:" + first);
                        String urlForParse = first.replace("1.html", "");

                        String lastLink = urlForParse + "10000.html";

                        System.out.println(lastLink);
                        Document lastPage = ParserHelper.getDocument(lastLink);

                        System.out.println(lastPage.location());

                        Element elementAmount = lastPage.select("ul.pagination > li.active a").first();

                        if (elementAmount != null) {
                            int amount = Integer.parseInt(elementAmount.text());

                            System.out.println(amount);
                            return IntStream.range(1, amount + 1).mapToObj(i -> urlForParse + i + ".html").toList();
                        } else System.out.println("Не вийшло отримати кількість сторінок");

                    }
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
                .book()
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


    /*public static Parser createAvidreadersParser() {

        return new ParserBuilder()
                .book()
                    .title("body > div.page_container > div.container.main_container > div.wrap_cols.clear > div.left_clmn > div.wrap_book.clear > div.book_info.overflow > h1")
                    .description("body > div.page_container > div.container.main_container > div.wrap_cols.clear > div.left_clmn > div.wrap_description.no_margin > p")

                .and()
                .chapters()
                    .deleteElements("")
                    .links(((bookUrl, main) -> {


                        Element firstLink = main.select("body > div.page_container > div.container.main_container > div.wrap_cols.clear > div.right_clmn > a").first();

                        if (firstLink != null) {


                            String firstPageLink = firstLink.absUrl("href");

                            Document fistPage = ParserHelper.getDocument(firstPageLink);

                            List<String> links = new ArrayList<>();
                            String[] text = fistPage.select("#viewer > div.page_settings > div.page_num").first().text().split(" ");


                            if (text.length == 3) {
                                int amount = Integer.parseInt(text[2]);


                                for(int p = 1; p <= amount; p++) {
                                    links.add(firstPageLink + "?p=" + p);
                                }
                                return links;
                            }
                        }

                        return null;
                    }))

                .and()
                .build();
    }*/

    public static Parser create4italkaParser() {

        return new ParserBuilder()
                .books()
                .author("#page > main > div.author-books > div > div > div.short-recent-items.extended.boxes-6.figure-m-v-10 > div> div.desc-content > h4 > a")
                .and()
                .book()
                .title("#page > main > div.about-book > div.about-book__desc > div.about-book__desc-info > h1")
                .description("#page > main > div.about-book > div.about-book__desc > div.about-book__desc-info > div.about-book__desc-info-txt > p")
                .and()
                .chapters()
                .links(((bookUrl, main) -> List.of(bookUrl)))
                .chapter()
                .title("h3")
                .selector("#book > div > div > div.toread-text.m-v-30 > div.text-content.box-md-8.m-auto > *")
                .back()
                .and()
                .build();
    }
}
