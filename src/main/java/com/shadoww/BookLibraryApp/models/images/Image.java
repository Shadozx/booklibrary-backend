package com.shadoww.BookLibraryApp.models.images;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
//@Table(name = "images")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
@Getter
@Setter
@NoArgsConstructor
public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;


    private String contentType = "image/jpeg";

    @Column(columnDefinition = "bytea")
    private byte[] data;


    @Column(unique = true)
    @NotBlank(message = "Filename cannot be empty")
    private String filename;

    @Transient
    private String url;


    public Image(String filename) {
        this.filename = filename;
    }


    private static String getStandardImageUrl() {
        return "http://localhost:9091/api/media/";
    }


    public static String getFileNameFromImg(String src) {
        return src.startsWith(getStandardImageUrl()) ? src.replace(getStandardImageUrl(), "") : null;
    }

    public String getUrl() {
        return getStandardImageUrl() + filename;
    }
}
