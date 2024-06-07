package com.shadoww.BookLibraryApp.controllers.comments;


import com.shadoww.BookLibraryApp.dto.response.comments.BookCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comments.ChapterCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comments.CommentResponse;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.comments.BookComment;
import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.ChaptersService;
import com.shadoww.BookLibraryApp.services.CommentsService;
import com.shadoww.BookLibraryApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/comments")
public class ApiCommentsController {

    private final CommentsService commentsService;

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final ChaptersService chaptersService;

    @Autowired
    public ApiCommentsController(CommentsService commentsService,
                                     PeopleService peopleService,
                                     BooksService booksService,
                                     ChaptersService chaptersService) {
        this.commentsService = commentsService;
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.chaptersService = chaptersService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentsService.findAllComments()
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

    @GetMapping("/books/{bookId}")
    public List<BookCommentResponse> getBookCommentsByBookAndOwner(@PathVariable int bookId) {
        return commentsService.finByBook(booksService.readById(bookId))
                .stream()
                .map(BookCommentResponse::new)
                .toList();
    }


    @GetMapping("/chapters/{chapterId}")
    public List<ChapterCommentResponse> getBookCommentsByChapterAndOwner(@PathVariable int chapterId) {
        return commentsService.finByChapter(chaptersService.readById(chapterId))
                .stream()
                .map(ChapterCommentResponse::new)
                .toList();
    }


}
