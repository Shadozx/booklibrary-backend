package com.shadoww.BookLibraryApp.dto.response.comments;

import com.shadoww.BookLibraryApp.models.comments.Comment;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentResponse {

    long id;

    String text;

    LocalDateTime createdAt;

    long ownerId;

    public CommentResponse(Comment comment) {

        this.id = comment.getId();
        this.text = comment.getText();
        this.ownerId = comment.getOwner().getId();
        this.createdAt = comment.getCreatedAt();
    }
}
