package com.shadoww.BookLibraryApp.util.parser.interfaces;

import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Stack;

public interface TextConvector {


    void transform(Stack<ChapterInstance> chapterInstances, Element el);
}
