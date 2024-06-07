package com.shadoww.BookLibraryApp.controllers;


import com.shadoww.BookLibraryApp.models.images.Image;
import com.shadoww.BookLibraryApp.services.ImagesService;
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

    private final ImagesService imagesService;

    @Autowired
    public ApiImagesController(ImagesService imagesService) {
        this.imagesService = imagesService;
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getBookImageData(@PathVariable String filename)  {

        Optional<Image> foundImage = imagesService.findImageByFilename(filename);
        if(foundImage.isEmpty()) {
            return ResponseImage.noFound();
        }

        Image image = foundImage.get();


        return ResponseEntity.ok().contentType(MediaType.valueOf(image.getContentType())).body(image.getData());
    }
}

