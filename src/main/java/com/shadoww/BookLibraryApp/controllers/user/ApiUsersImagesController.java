package com.shadoww.BookLibraryApp.controllers.user;

import com.shadoww.BookLibraryApp.models.images.PersonImage;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.services.ImagesService;
import com.shadoww.BookLibraryApp.services.PeopleService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseImage;
import com.shadoww.BookLibraryApp.util.responsers.ResponseUser;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/images")
public class ApiUsersImagesController {

    private final PeopleService peopleService;

    private final ImagesService imagesService;

    @Autowired
    public ApiUsersImagesController(PeopleService peopleService,
                                    ImagesService imagesService) {
        this.peopleService = peopleService;
        this.imagesService = imagesService;
    }


    @PreAuthorize("hasRole('ADMIN') || #userId == principal.id")
    @PostMapping
    public ResponseEntity<?> addImage(@PathVariable int userId,
                                      @RequestBody MultipartFile file) throws IOException {


        if (file == null) return ResponseImage.noContent();

        Person person = peopleService.readById(userId);


        PersonImage personImage = person.getPersonImage();

        if (personImage == null) {

            personImage = new PersonImage();

            personImage.setFilename("user_" + person.getId() + ".jpeg");

            person.setPersonImage(personImage);
            personImage.setOwner(person);

        }

        personImage.setData(file.getBytes());

        imagesService.save(personImage);

        peopleService.update(person);

        return ResponseImage.addSuccess();

    }
}
