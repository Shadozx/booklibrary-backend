package com.shadoww.BookLibraryApp.util.texformatters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.models.images.Image;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElement;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormatter {

    private TextFormatter() {}


    /**
    * То що приходить з html редактора перетворюється в паттерн елементів
    *
    **/

    public static TextElements parse(String html) {

        return formatToPatternsText(html);
    }

//    public static TextElements parseOne() {
//
//        return null;
//    }



    /**
     *
     * Використовується в парсерах для парсингу по елементно
     * */
    public static TextElement parse(Element element, ElementType type) {
        return formatPatternText(element, type);
    }


    /**
     * Для перегляду тексту в формі html
     * */
    public static TextElements parsePatterns(String patternText) {
        String reg ="\\[(?<tag>\\w+)(, attrs=\"(?<attrs>.*)\")?\\]";

        Matcher matcher = Pattern.compile(reg).matcher(patternText);

        TextElements elements = new TextElements();
        while(matcher.find()) {
            String tag = matcher.group("tag");
            String attrs = matcher.group("attrs");
            ElementType type = ElementType.valueOf(tag);
            TextElement element;//new TextElement(ElementType.valueOf(tag),getAttributes(attrs));
            Map<String, String> attributes = getAttributes(attrs);
            switch (type) {
                case Image -> {
                    String filename = attributes.get("filename");

                    if (filename != null && !filename.equals("")) attributes.put("filename", new ChapterImage(filename).getUrl());

                    element = new TextElement(type, attributes);

                }
                default -> element = new TextElement(type, attributes);
            }


//            String  attributes = attrs.substring(attrs.indexOf("\"") + 1, attrs.lastIndexOf("\""));

//            System.out.println(attributes);

            elements.add(element);
        }
        return elements;
    }


    /**
     *
     * Для оцінювання елементів
     * **/
    public static TextElements parsePatternText(String patternText) {
        String reg ="\\[(?<tag>\\w+)(, attrs=\"(?<attrs>.*)\")?\\]";

        Matcher matcher = Pattern.compile(reg).matcher(patternText);

        TextElements elements = new TextElements();
        while(matcher.find()) {
            String tag = matcher.group("tag");
            String attrs = matcher.group("attrs");
            ElementType type = ElementType.valueOf(tag);
            TextElement element;//new TextElement(ElementType.valueOf(tag),getAttributes(attrs));
            Map<String, String> attributes = getAttributes(attrs);
            switch (type) {
                case Image -> {
                    String filename = attributes.get("filename");

                    if (filename != null && !filename.equals("")) attributes.put("filename", filename);

                    element = new TextElement(type, attributes);

                }
                default -> element = new TextElement(type, attributes);
            }


//            String  attributes = attrs.substring(attrs.indexOf("\"") + 1, attrs.lastIndexOf("\""));

//            System.out.println(attributes);

            elements.add(element);
        }
        return elements;
    }


    // щоб поміняти паттерн текст на html
    public static List<Chapter> parsePatternText(List<Chapter> chapters) {
        for(var c : chapters) {
            c.setText(parsePatterns(c.getText()).html());
        }
        return chapters;
    }

    private static Map<String, String> getAttributes(String attributes) {


        try {

            return new ObjectMapper().readValue(attributes, Map.class);
        }catch (IOException e) {
            System.out.println("Message: " + e.getMessage() + ".");
            return new HashMap<>();
        }
//        String reg = "(?<key>\\w+):(?<value>.*)";
//        Matcher matcher = Pattern.compile(reg).matcher(attributes);
//
//        while (matcher.find()) {
//            String key = matcher.group("key");
//            String value = matcher.group("value");
//            System.out.println("Key:" + key + ".Value:"+value);
//            if (!key.equals("") && !value.equals("")) {
//                attrs.put(key, value);
//            }
//        }

    }

    /**
     * З хтмл текст в паттерновий текст
     * */
    private static TextElements formatToPatternsText(String html) {
//        Matcher matcher = Pattern.compile("<(?<tag>\\w+) (src=\"(?<filename>.+?)\")? .*?>((?<text>.+?)</(\\k<tag>)>)?").matcher(html);

        Jsoup.parse(html).select("br").remove();
        List<Element> els = Jsoup.parse(html).select("p, img").stream().filter(e-> !(e.tagName().equals("p") &&  e.children().select("img").size() > 0)).toList();



        TextElements elements = new TextElements();

//        List<Element> filteredElements = els.filter(e->e.parent().tagName())
        for(Element el : els) {
            TextElement element = null;
//            String element = "";
            if (el.tagName().equals("p")) {
                element = new TextElement(ElementType.Paragraph);
                element.text( el.html());
//                element = "[text=\""+ el.html() + "\"]";
            }

            if (el.tagName().equals("img")) {
                element = new TextElement(ElementType.Image);


                if (el.hasAttr("data-filename")) {

                    element.addAttribute("data-filename", el.attr("data-filename"));
                }

                String src = el.attr("src");

                String filename = ChapterImage.getFileNameFromImg(src);

                if (filename != null && !filename.equals("")) {
                    element.addAttribute("filename", filename);
                } else {
//                    System.out.println(src);

                    Matcher matcher = Pattern.compile("base64,(?<data>.+)").matcher(src);
                    if (matcher.find()) {
                        String data = matcher.group("data");

                        element.addAttribute("data", data);
                    }
                }
//                element.addAttribute("filename", el.attr("src"));


//                element = "[photo=\"" + el.attr("src") + "\"]";
            }

            if (!el.className().equals("") && element != null) element.flag(el.className());
            if (element!= null) elements.add(element);


        }

        return elements;
    }


    private static String formatToPatterns(String html) {
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

//                System.out.println(filename);
                element = "[photo=\"" + el.attr("src") + "\""; //+ "\"]");
            }

            if (element != "" && flags != "") element+=", flags=\"" + flags + "\"]";

            if (element != "" && flags == "") element+= "]";

            if (element != "") patterns.add(element);
        }

        return String.join("\n", patterns);
    }


    private static TextElement formatPatternText(Element el, ElementType type) {
//        Matcher matcher = Pattern.compile("<(?<tag>\\w+) (src=\"(?<filename>.+?)\")? .*?>((?<text>.+?)</(\\k<tag>)>)?").matcher(html);

        switch (type) {

            case Paragraph -> {
                TextElement element = new TextElement(ElementType.Paragraph);

                if (!el.html().equals("")) {
//                    element.addAttribute("text", el.html());

                    element.text(el.html());

                    return element;

                }else return null;


            }
            case Image -> {
                TextElement element = new TextElement(ElementType.Image);
                element.addAttribute("filename", el.attr("src"));

                return element;
//            "[photo=\"" + el.attr("src") + "\"]";

            }
            default -> {
                TextElement element = new TextElement(ElementType.Other);

                if(!el.html().equals("")) {
//                    element.addAttribute("text", el.toString());
                    element.text(el.toString());

//                "[other=\"" + el.toString() + "\"]" :"";
                }else element.text("");

                return element;
            }
        }

    }

}
