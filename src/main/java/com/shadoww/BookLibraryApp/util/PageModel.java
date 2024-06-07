package com.shadoww.BookLibraryApp.util;

import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.Chapter;
import org.springframework.ui.Model;

import java.util.List;

public class PageModel {
    private Model model;

    public PageModel(Model model) {
        this.model = model;
    }


    public void chapters(List<Chapter> chapters) {
        model.addAttribute("chapters", chapters);
    }

    public void catalogs(List<BookCatalog> catalogs) {
        model.addAttribute("catalogs", catalogs);
    }

    public void mark(BookMark mark) {
        model.addAttribute("mark", mark);
    }

    public void book(Book book) {
        model.addAttribute("book", book);
    }

    public void paragraph(int paragraph) {
        model.addAttribute("paragraph", paragraph);

    }
    public void counter(Counter counter) {
        model.addAttribute("counter", counter);
    }

    public void currenChapter(int current) {
        model.addAttribute("currentChapter", current);
    }

    public void totalPages(int totalPages) {
        model.addAttribute("totalChapters", totalPages);
    }
}
