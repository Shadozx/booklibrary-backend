package com.shadoww.BookLibraryApp.controller.comment;


import com.shadoww.BookLibraryApp.dto.response.comment.BookCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comment.ChapterCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comment.CommentResponse;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
import com.shadoww.BookLibraryApp.service.interfaces.CommentService;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/comments")
public class ApiCommentsController {

    private final CommentService commentService;

    private final PersonService personService;
    private final BookService bookService;
    private final ChapterService chapterService;

    @Autowired
    public ApiCommentsController(CommentService commentService,
                                 PersonService personService,
                                 BookService bookService,
                                 ChapterService chapterService) {
        this.commentService = commentService;
        this.personService = personService;
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAll()
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

    @GetMapping("/books/{bookId}")
    public List<BookCommentResponse> getBookCommentsByBookAndOwner(@PathVariable long bookId) {
        return commentService.getBookComments(bookService.readById(bookId))
                .stream()
                .map(BookCommentResponse::new)
                .toList();
    }

    @GetMapping("/chapters/{chapterId}")
    public List<ChapterCommentResponse> getBookCommentsByChapterAndOwner(@PathVariable long chapterId) {
        return commentService.getChapterComments(chapterService.readById(chapterId))
                .stream()
                .map(ChapterCommentResponse::new)
                .toList();
    }
}
