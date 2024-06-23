package com.shadoww.BookLibraryApp.model.image;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
    private Long id;

    // default value content type is image/jpeg
    private String contentType = "image/jpeg";

//    @NotNull(message = "Image must have data")
    @Column(columnDefinition = "bytea", nullable = false)
    private byte[] data;

    @NotBlank(message = "Filename cannot be empty")
    @Column(unique = true, nullable = false)
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
