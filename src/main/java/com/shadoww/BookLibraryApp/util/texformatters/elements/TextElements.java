package com.shadoww.BookLibraryApp.util.texformatters.elements;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Массив елементів тексту
 * <p>
 * */
public class TextElements extends ArrayList<TextElement>{

    public TextElements() {
        super();
    }

    public TextElements(int initialCapacity) {
        super(initialCapacity);
    }

    public TextElements(Collection<TextElement> elements) {
        super(elements);
    }


    public TextElement first() {
//        return this.elements.get(0);
        return !this.isEmpty() ? this.get(0) : null;
    }

    public TextElement last() {
        return !this.isEmpty() ? this.get(this.size()-1) : null;
    }

    public TextElement get(int i) {
        return super.get(i);
    }

    public boolean isEmpty() {
        return super.isEmpty();
    }

    public boolean add(TextElement element) {
        return super.add(element);
    }

    public boolean addAll(Collection<? extends TextElement> elements) {
        return super.addAll(elements);
    }

    public boolean delete(TextElement element) {
        return super.remove(element);
    }

    public TextElement delete(int i) {
        return super.remove(i);
    }

    public String html() {
        return String.join("\n", super.stream().map(TextElement::html).toList());
    }

    public String toPatternText() {
        return String.join("\n", super.stream().map(TextElement::toPatternText).toList());
    }

    @Override
    public String toString() {
        return this.toPatternText();
    }
}
