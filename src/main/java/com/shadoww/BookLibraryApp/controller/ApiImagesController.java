package com.shadoww.BookLibraryApp.controller;


import com.shadoww.BookLibraryApp.model.image.Image;
import com.shadoww.BookLibraryApp.service.interfaces.ImageService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/media")

public class ApiImagesController {

    private final ImageService imageService;

    @Autowired
    public ApiImagesController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getBookImageData(@PathVariable String filename)  {

        Image image = imageService.getImageByFilename(filename);


        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getContentType())).body(image.getData());
    }
}

