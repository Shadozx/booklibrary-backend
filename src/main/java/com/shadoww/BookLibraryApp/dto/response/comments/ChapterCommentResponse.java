package com.shadoww.BookLibraryApp.dto.response.comments;

import com.shadoww.BookLibraryApp.models.comments.ChapterComment;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class ChapterCommentResponse {

    long id;

    String text;

    LocalDateTime createdAt;

    long ownerId;

    long chapterId;

    public ChapterCommentResponse(ChapterComment comment) {

        this.id = comment.getId();
        this.text = comment.getText();
        this.ownerId = comment.getOwner().getId();
        this.createdAt = comment.getCreatedAt();
        this.chapterId = comment.getChapter().getId();
    }
}
