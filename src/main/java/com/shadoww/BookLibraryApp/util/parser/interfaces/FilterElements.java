package com.shadoww.BookLibraryApp.util.parser.interfaces;

import org.jsoup.nodes.Element;

import javax.lang.model.util.Elements;

public interface FilterElements {

    boolean filter(Element element);
}
