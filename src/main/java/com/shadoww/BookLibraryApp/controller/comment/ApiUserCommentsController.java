package com.shadoww.BookLibraryApp.controller.comment;


import com.shadoww.BookLibraryApp.dto.request.comment.CommentRequest;
import com.shadoww.BookLibraryApp.dto.response.comment.BookCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comment.ChapterCommentResponse;
import com.shadoww.BookLibraryApp.model.comment.BookComment;
import com.shadoww.BookLibraryApp.model.comment.ChapterComment;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.ChapterService;
import com.shadoww.BookLibraryApp.service.interfaces.CommentService;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/comments")
public class ApiUserCommentsController {

    private final CommentService commentService;

    private final PersonService personService;
    private final BookService bookService;
    private final ChapterService chapterService;

    @Autowired
    public ApiUserCommentsController(CommentService commentService,
                                     PersonService personService,
                                     BookService bookService,
                                     ChapterService chapterService) {
        this.commentService = commentService;
        this.personService = personService;
        this.bookService = bookService;
        this.chapterService = chapterService;
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> createBookComment(@PathVariable long userId,
                                               @PathVariable long bookId,
                                               @RequestBody CommentRequest request) {
        BookComment comment = new BookComment();
        comment.setText(request.getText());
        comment.setBook(bookService.readById(bookId));
        comment.setOwner(personService.readById(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookCommentResponse((BookComment) commentService.create(comment)));
    }

    @GetMapping("/books/{bookId}")
    public List<BookCommentResponse> getBookCommentsByBookAndOwner(@PathVariable long userId,
                                                                   @PathVariable long bookId) {
        return commentService.getOwnerBookComments(bookService.readById(bookId), personService.readById(userId))
                .stream()
                .map(BookCommentResponse::new)
                .toList();
    }


    @PostMapping("/chapters/{chapterId}")
    public ResponseEntity<?> createChapterComment(@PathVariable long userId,
                                                  @PathVariable long chapterId,
                                                  @RequestBody CommentRequest request) {
        ChapterComment comment = new ChapterComment();
        comment.setText(request.getText());
        comment.setChapter(chapterService.readById(chapterId));
        comment.setOwner(personService.readById(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ChapterCommentResponse((ChapterComment) commentService.create(comment)));
    }

    @GetMapping("/chapters/{chapterId}")
    public List<ChapterCommentResponse> getBookCommentsByChapterAndOwner(@PathVariable long userId,
                                                                         @PathVariable long chapterId) {
        return commentService.getOwnerChapterComments(chapterService.readById(chapterId), personService.readById(userId))
                .stream()
                .map(ChapterCommentResponse::new)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN') || #userId == principal.id")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable String userId,
                                               @PathVariable long commentId) {

        commentService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

}
