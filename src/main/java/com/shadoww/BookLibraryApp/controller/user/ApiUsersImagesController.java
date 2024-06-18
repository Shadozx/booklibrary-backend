package com.shadoww.BookLibraryApp.controller.user;

import com.shadoww.BookLibraryApp.model.image.PersonImage;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.interfaces.ImageService;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/images")
public class ApiUsersImagesController {

    private final PersonService personService;

    private final ImageService imageService;

    @Autowired
    public ApiUsersImagesController(PersonService personService,
                                    ImageService imageService) {
        this.personService = personService;
        this.imageService = imageService;
    }


    @PreAuthorize("hasRole('ADMIN') || #userId == principal.id")
    @PostMapping
    public ResponseEntity<?> addImage(@PathVariable long userId,
                                      @RequestBody MultipartFile file) throws IOException {


        if (file == null) return ResponseImage.noContent();

        Person person = personService.readById(userId);


        PersonImage personImage = person.getPersonImage();

        if (personImage == null) {

            personImage = new PersonImage();

            personImage.setFilename("user_" + person.getId() + ".jpeg");

            person.setPersonImage(personImage);
            personImage.setOwner(person);

        }

        personImage.setData(file.getBytes());

        imageService.create(personImage);

        personService.update(person);

        return ResponseImage.addSuccess();

    }
}
