package com.shadoww.BookLibraryApp.model.comment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.model.Chapter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Chapter")
@Setter
@Getter
public class ChapterComment extends Comment {

    @JsonIgnore
    @ManyToOne(optional = true)
    @JoinColumn(name = "chapter_id", nullable = true)
    private Chapter chapter;


}