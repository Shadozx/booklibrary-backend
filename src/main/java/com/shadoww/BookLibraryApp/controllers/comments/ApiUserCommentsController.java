package com.shadoww.BookLibraryApp.controllers.comments;


import com.shadoww.BookLibraryApp.dto.request.comments.CommentRequest;
import com.shadoww.BookLibraryApp.dto.response.comments.BookCommentResponse;
import com.shadoww.BookLibraryApp.dto.response.comments.ChapterCommentResponse;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.comments.BookComment;
import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.ChaptersService;
import com.shadoww.BookLibraryApp.services.CommentsService;
import com.shadoww.BookLibraryApp.services.PeopleService;
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

    private final CommentsService commentsService;

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final ChaptersService chaptersService;

    @Autowired
    public ApiUserCommentsController(CommentsService commentsService,
                                     PeopleService peopleService,
                                     BooksService booksService,
                                     ChaptersService chaptersService) {
        this.commentsService = commentsService;
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.chaptersService = chaptersService;
    }

    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> createBookComment(@PathVariable int userId, @PathVariable int bookId, @RequestBody CommentRequest request) {
        BookComment comment = new BookComment();
        comment.setText(request.getText());
        comment.setBook(booksService.readById(bookId));
        comment.setOwner(peopleService.readById(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookCommentResponse((BookComment) commentsService.create(comment)));
    }

    @GetMapping("/books/{bookId}")
    public List<BookCommentResponse> getBookCommentsByBookAndOwner(@PathVariable int userId, @PathVariable int bookId) {
        return commentsService.finByBookAndOwner(booksService.readById(bookId), peopleService.readById(userId))
                .stream()
                .map(BookCommentResponse::new)
                .toList();
    }


    @PostMapping("/chapters/{chapterId}")
    public ResponseEntity<?> createChapterComment(@PathVariable int userId, @PathVariable int chapterId, @RequestBody CommentRequest request) {
        ChapterComment comment = new ChapterComment();
        comment.setText(request.getText());
        comment.setChapter(chaptersService.readById(chapterId));
        comment.setOwner(peopleService.readById(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(new ChapterCommentResponse((ChapterComment) commentsService.create(comment)));
    }

    @GetMapping("/chapters/{chapterId}")
    public List<ChapterCommentResponse> getBookCommentsByChapterAndOwner(@PathVariable int userId, @PathVariable int chapterId) {
        return commentsService.finByChapterAndOwner(chaptersService.readById(chapterId), peopleService.readById(userId))
                .stream()
                .map(ChapterCommentResponse::new)
                .toList();
    }

//    @PreAuthorize("hasRole('ADMIN') || #userId == principal.id")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable String userId, @PathVariable int commentId) {
//        peopleService.deleteById(commentId);

        commentsService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

}
