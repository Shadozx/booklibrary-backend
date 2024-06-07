package com.shadoww.BookLibraryApp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadoww.BookLibraryApp.auth.JwtAuthenticationFilter;
import com.shadoww.BookLibraryApp.auth.service.JwtService;
import com.shadoww.BookLibraryApp.dto.request.ChapterRequest;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.Chapter;

import com.shadoww.BookLibraryApp.testservices.BooksService;
import com.shadoww.BookLibraryApp.testservices.ChaptersService;

import com.shadoww.BookLibraryApp.testservices.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApiChaptersController.class)
class ApiChaptersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private PeopleService peopleService;

    @MockBean
    private BooksService booksService;

    @MockBean
    private ChaptersService chaptersService;

    private Book book;
    private Chapter chapter;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setTitle("Test Book");

        chapter = new Chapter();
        chapter.setId(1);
        chapter.setTitle("Test Chapter");
        chapter.setBook(book);
    }

    @Test
    void testGetChaptersByBook() throws Exception {
        book.setChapters(List.of(chapter));

        when(booksService.readById(1)).thenReturn(book);

        mockMvc.perform(get("/api/books/1/chapters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chapter.getId()))
                .andExpect(jsonPath("$[0].title").value(chapter.getTitle()));
    }

    @Test
    void testAddChapter() throws Exception {
        ChapterRequest chapterRequest = new ChapterRequest();
        chapterRequest.setTitle("New Chapter");
        chapterRequest.setText("Some text");
        chapterRequest.setNumberOfPage(1);

        when(booksService.readById(1)).thenReturn(book);

        mockMvc.perform(post("/api/books/1/chapters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Chapter was successfully added"));
    }

    @Test
    void testGetChapter() throws Exception {
        when(booksService.readById(1)).thenReturn(book);
        when(chaptersService.readById(1)).thenReturn(chapter);

        mockMvc.perform(get("/api/books/1/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chapter.getId()))
                .andExpect(jsonPath("$.title").value(chapter.getTitle()));
    }

    @Test
    void testGetChapterByBookAndChapterNumber() throws Exception {
        chapter.setNumberOfPage(1);
        when(booksService.readById(1)).thenReturn(book);
        when(chaptersService.findByBookAndNumber(eq(book), eq(1))).thenReturn(chapter);

        mockMvc.perform(get("/api/books/1/chapters/number/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfPage").value(chapter.getNumberOfPage()))
                .andExpect(jsonPath("$.title").value(chapter.getTitle()));
    }

    @Test
    void testUpdateChapter() throws Exception {
        ChapterRequest chapterRequest = new ChapterRequest();
        chapterRequest.setTitle("Updated Chapter");
        chapterRequest.setText("Updated text");
        chapterRequest.setNumberOfPage(2);

        when(booksService.readById(1)).thenReturn(book);
        when(chaptersService.readById(1)).thenReturn(chapter);

        mockMvc.perform(put("/api/books/1/chapters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chapterRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Chapter was successfully updated"));
    }

    @Test
    void testDeleteChapter() throws Exception {
        when(booksService.readById(1)).thenReturn(book);
        when(chaptersService.readById(1)).thenReturn(chapter);

        mockMvc.perform(delete("/api/books/1/chapters/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chapter was successfully deleted"));

        Mockito.verify(booksService, Mockito.times(1)).deleteChapter(chapter);
    }
}
