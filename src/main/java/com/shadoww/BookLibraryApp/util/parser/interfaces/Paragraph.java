package com.shadoww.BookLibraryApp.util.parser.interfaces;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.images.ChapterImage;
import com.shadoww.BookLibraryApp.util.instances.ChapterInstance;
import com.shadoww.BookLibraryApp.util.texformatters.elements.TextElements;
import org.jsoup.nodes.Element;

import java.util.List;

public interface Paragraph {

    TextElements getParagraph(Element element, ChapterInstance current, List<ChapterImage> chapterImages, Book book);
}
