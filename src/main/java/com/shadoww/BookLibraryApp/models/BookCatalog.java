package com.shadoww.BookLibraryApp.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.dto.request.BookCatalogRequest;
import com.shadoww.BookLibraryApp.models.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.io.Serializable;
import java.util.List;

@Entity
//@Table(name = "catalogs")
@Setter
@Getter
@NoArgsConstructor
public class BookCatalog implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotBlank(message = "Title cannot be empty")
    private String title;


    private boolean isPublic = true;

    @JsonIgnore
    @ManyToOne()
    @JoinColumn(name = "owner")
    private Person owner;


    @JsonIgnore
    @OneToMany(mappedBy = "catalog")
    @Cascade({org.hibernate.annotations.CascadeType.REMOVE})
    private List<BookMark> bookMarks;



    public BookCatalog(BookCatalogRequest bookCatalogRequest) {
        this.setTitle(bookCatalogRequest.getTitle());
        this.setIsPublic(bookCatalogRequest.getIsPublic());
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    public boolean getIsPublic() {
        return isPublic;
    }

    @Override
    public String toString() {
        return "BookCatalogResponse{" +
                "title='" + title + '\'' +
                ", isPublic=" + isPublic +
                '}';
    }
}
