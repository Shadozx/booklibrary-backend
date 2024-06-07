package com.shadoww.BookLibraryApp.util.parser.interfaces;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public interface ChapterLinks {
    List<String> getChapterLinks(String bookUrl, Document main) throws IOException;
}

