package com.shadoww.BookLibraryApp.util.texformatters.elements;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadoww.BookLibraryApp.util.texformatters.types.ElementType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Елемент тексту
 *
 * */
public class TextElement {

    private ElementType type;

    private Map<String, String> attributes;


//    private List<String> flags;
    public TextElement(ElementType type) {
        this.type = type;

        this.attributes = new HashMap<>();
//        this.flags = new ArrayList<>();
    }

    public TextElement(ElementType type, Map<String, String> attributes){
        this.type = type;

        this.attributes = attributes;
//        this.flags = new ArrayList<>();
    }
    public boolean hasAttribute(String attribute) {
        return this.attributes.containsKey(attribute);
    }

    public boolean hasType(ElementType type) {
        return this.type.equals(type);
    }

    public boolean hasFlags() {
        return this.hasAttribute("flags");
    }
    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public String attr(String key) {
        return this.hasAttribute(key) ? this.attributes.get(key) : null;
    }

    public Map<String, String> attributes() {
        return this.attributes;
    }


    public String text(String value) {
        return this.attributes.put("text", value);
    }

    public String flag(String value) {

        return this.hasAttribute("flags") ? this.attributes.put("flags", this.attributes.get("flags") + " " + value) : this.attributes.put("flags", value);
    }

    public String flags() {
        return  this.hasAttribute("flags") ? this.attributes.get("flags") : null;
    }


    public String deleteAttribute(String attr) {
        return this.attributes.remove(attr);
    }


    public String html() {
        Document html = Jsoup.parse("");



        if (this.hasType(ElementType.Paragraph)) {

            Element e = html.appendElement("p");

            String  text = this.attr("text");

            if(this.hasFlags()) e.attr("class", this.flags());

            if (text != null) e.html(text);

            return e.toString();

        }
        else if (this.hasType(ElementType.Image)) {
            String filename = this.attr("filename");
            Element e = html.appendElement("img").attr("src", filename);
                    //.attr("loading", "lazy");

            if (this.hasFlags()) e.attr("class", this.flags());

            return e.toString();

        }
        else if (this.hasType(ElementType.Other)){
            String text = this.attr("text");

//            System.out.println(text);
            Element e = html.appendElement("p");

            if (text != null) e.html(text);

            return e.toString();

        }else {
            return "";
        }
    }

    public String toPatternText() {
        //        return "[" + type + ", attrs=\""+ attributes.entrySet().stream().map(entry->entry.getKey() + ":" + entry.getValue()).collect(Collectors.joining(", ")) +
//                "\"]";
        try {
            return "[" + type + ", attrs=\"" + (this.attributes.size() > 0 ? new ObjectMapper().writeValueAsString(this.attributes) : "") +
                    "\"]";
        }catch (IOException e) {
            System.out.println("Message: " + e.getMessage() + ". Something went wrong");
            return "";
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextElement element = (TextElement) o;
        return type == element.type && Objects.equals(attributes, element.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, attributes);
    }

    @Override
    public String toString() {
        return toPatternText();
    }
}
