package com.shadoww.BookLibraryApp.util.formatters;

import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class _222TextFormatter {

    private _222TextFormatter() {}

    public static String formatToHtml(String text) {

        String reg ="\\[(?<tag>\\w+)=\"(?<text>.+?)\"(?:, flags=\"(?<flags>.+)\")?\\]";

        Document html = Jsoup.parse("");

        Matcher matcher = Pattern.compile(reg).matcher(text);

        List<String> elements = new ArrayList<>();
        while (matcher.find()) {

            String tag = matcher.group("tag");
            String p_text = matcher.group("text");
            String flags = matcher.group("flags");
//            System.out.println("Tag: " + tag + ". Text: " + p_text + ". Flags: " + flags);


            if (Objects.equals(tag, "text")) {
                Element e = html.appendElement("p").append(p_text);

                if (flags != "" && flags != null) e.attr("class", flags);

                elements.add(e.toString());

            }
            else if (Objects.equals(tag, "photo")) {
                Image image = new Image();
                image.setFilename(p_text);
                Element e = html.appendElement("img").attr("src", image.getUrl()).attr("loading","lazy");


                if (flags != "" && flags != null) e.attr("class", flags);

                elements.add(e.toString());

            }else if (Objects.equals(tag, "other")) {
                Element e = html.appendElement("p").append(p_text);

                elements.add(e.toString());
            }
        }

        return String.join("\n", elements);
    }

    public static List<Chapter> formatToHtml(List<Chapter> chapters) {
        for(var chapter : chapters) {
            chapter.setText(formatToHtml(chapter.getText()));
        }
        return chapters;
    }
    public static String formatToPatterns(String html) {
//        Matcher matcher = Pattern.compile("<(?<tag>\\w+) (src=\"(?<filename>.+?)\")? .*?>((?<text>.+?)</(\\k<tag>)>)?").matcher(html);

        Elements els = Jsoup.parse(html).select("p, img");

        List<String> patterns = new ArrayList<>();
        for(Element el : els) {
            String flags =  el.className();

            String element = "";
            if (el.tagName().equals("p")) {
                element = "[text=\""+ el.html() + "\""; //+ "\"]");
            }
            if (el.tagName().equals("img")) {
                String filename = Image.getFileNameFromImg(el.attr("src"));

                System.out.println(filename);
                element = "[photo=\"" + el.attr("src") + "\""; //+ "\"]");
            }

            if (element != "" && flags != "") element+=", flags=\"" + flags + "\"]";

            if (element != "" && flags == "") element+= "]";

            if (element != "") patterns.add(element);
        }

        return String.join("\n", patterns);
    }

    public static String formatToPatternsText(String html) {
//        Matcher matcher = Pattern.compile("<(?<tag>\\w+) (src=\"(?<filename>.+?)\")? .*?>((?<text>.+?)</(\\k<tag>)>)?").matcher(html);

        Elements els = Jsoup.parse(html).select("p, img");


        List<String> patterns = new ArrayList<>();
        for(Element el : els) {

            String element = "";
            if (el.tagName().equals("p")) {
                element = "[text=\""+ el.html() + "\"]";
            }
            if (el.tagName().equals("img")) {
                element = "[photo=\"" + el.attr("src") + "\"]";
            }
            if (element != "") patterns.add(element);
        }

        return String.join("\n", patterns);
    }

    public static String formatPatternText(Element el) {
//        Matcher matcher = Pattern.compile("<(?<tag>\\w+) (src=\"(?<filename>.+?)\")? .*?>((?<text>.+?)</(\\k<tag>)>)?").matcher(html);


        String element;
//        System.out.println("Element: " + el.toString() + "Html: " + el.html() + ". ToString: " + el);
        if (el.tagName().equals("p") || el.tagName().equals("h3")) element = !el.html().equals("") ?  "[text=\""+ el.html() + "\"]" : "";

        else if (el.tagName().equals("img")) element = "[photo=\"" + el.attr("src") + "\"]";

        else element = !el.html().equals("") ? "[other=\"" + el.toString() + "\"]" : "";

//            if (element != "" && flags != "") element+=", flags=\"" + flags + "\"]";

//            if (element != "" && flags == "") element+= "]";

        return element;
    }

    /*
    * public List<ChapterInstance> parsePage(Document page) throws IOException {

        System.out.println("================================================================================");
//            String[] needElms = {"p", "div.take_h1", "div.em", "img"};
//
//            String selector = "body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.tb_read_book > div:nth-child(4) " + String.join(", ", needElms);
//        System.out.println(selector);
            List<Element> elements = page
//                    .select(selector);
                    .select("body > table > tbody > tr:nth-child(2) > td > table > tbody > tr > td.tb_read_book > div:nth-child(4)")
                    .select("p, div.take_h1, div.em, img")
                    .stream()
                    .filter(i->filterElements(i))
                    .toList();


            if(elements == null) throw new IOException("Elements is null!!!!");

//        System.out.println(elements);

//            List<ChapterInstance> chapters = new ArrayList<>();
//            List<String> chapter_names = new ArrayList<>();
//            for (Element el : elements) {
//
//                int size = chapters.size();
//                ChapterInstance current = size > 0 ? chapters.get(size - 1) : null;
//
//                if (el.hasClass("take_h1")) {
////                    ChapterInstance prev = size > 0 ? chapters.get(size-1);
//                    chapter_names.add(el.text());
////                    if (current != null && current.isEmpty()) current.addTitle(el.text());
//
////                    else {
////                        current = new ChapterInstance();
////                        current.setTitle(el.text());
////                        chapters.add(current);
////                    }
//                } else {
//
//                    if (current != null) current.addText(el.text());
//
//                    else {
//                        current = new ChapterInstance();
//                        current.addText(el.text());
//                        chapters.add(current);
//                    }
//                }
//
//            }
        Stack<ChapterInstance> chapters = new Stack<>();
        List<String> chapter_names = new ArrayList<>();

//        List<Element> els = elements.stream().filter(this::filterElements).toList();

        for(Element el : elements) {


            if(el.hasClass("take_h1")) {

                chapter_names.add(el.text());
            }
            else {
//                ChapterInstance current = new ChapterInstance();
//                if(!chapter_names.isEmpty()) {
//                    current.addTitle(String.join("\n", chapter_names));
//                    chapter_names.clear();
//                }
//
//                current.addText(el.text());
//
//
//
//                chapters.push(current);
                ChapterInstance current = null;

                if(!chapter_names.isEmpty()) {

                    current = new ChapterInstance();

                    current.addTitle(String.join("\n", chapter_names));
                    current.addText(el.text());

                    chapter_names.clear();
                }
                else {

                    current = !chapters.isEmpty() ? chapters.pop() : new ChapterInstance();

                    if(el.tagName().equals("p") && (el.parent().tagName().equals("div") &&  el.parent().hasClass("em"))) {

                    }
                    if(el.tagName().equals("img")) {
                        Image image = addChapterImage(el, this.images, this.book);

                        if(image != null) {
                            this.images.add(image);
                            current.addText("<img src=\"" + image.getUrl() + "\" class=\"mx-auto text-center\"/>");
                        }
                    }
                    else {



                        current.addText(el.text());
                    }
                }

                if(current != null ) chapters.push(current);

            }

        }


            System.out.println(chapters.size());
            return chapters;

    }
    * */
}
