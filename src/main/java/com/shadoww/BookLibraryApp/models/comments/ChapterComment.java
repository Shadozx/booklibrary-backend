package com.shadoww.BookLibraryApp.models.comments;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.Chapter;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("Chapter")
@Setter
@Getter
public class ChapterComment extends Comment {

    @JsonIgnore
    @OneToOne(optional = true)
    @JoinColumn(name = "chapter", nullable = true)
    private Chapter chapter;


}