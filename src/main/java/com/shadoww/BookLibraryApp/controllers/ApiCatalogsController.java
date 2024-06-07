package com.shadoww.BookLibraryApp.controllers;


import com.shadoww.BookLibraryApp.dto.response.BookCatalogResponse;
import com.shadoww.BookLibraryApp.dto.request.BookCatalogRequest;
import com.shadoww.BookLibraryApp.mapper.BookCatalogMapper;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookCatalog;
import com.shadoww.BookLibraryApp.models.BookMark;
import com.shadoww.BookLibraryApp.models.Chapter;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.services.*;
import com.shadoww.BookLibraryApp.util.responsers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/catalogs")
public class ApiCatalogsController {

    private final PeopleService peopleService;
    private final BookCatalogsService bookCatalogsService;

    @Autowired
    public ApiCatalogsController(PeopleService peopleService,
                                 BookCatalogsService bookCatalogsService) {
        this.peopleService = peopleService;
        this.bookCatalogsService = bookCatalogsService;
    }

    @GetMapping
    public ResponseEntity<?> getBookCatalogsByUser(@PathVariable int userId) {
        Person person = peopleService
                .readById(userId);

        return ResponseEntity.ok(
                person
                        .getCatalogs()
                        .stream()
                        .map(BookCatalogResponse::new)
                        .toList());
    }

    @PreAuthorize("#userId == principal.id")
    @PostMapping
    public ResponseEntity<?> addCatalog(@PathVariable int userId,
                                        @RequestBody BookCatalogRequest bookCatalogRequest) {

        if (bookCatalogRequest.isTitleEmpty()) return ResponseCatalog.noContent();

        Person owner = peopleService.readById(userId);

        BookCatalog catalog = new BookCatalog(bookCatalogRequest);

        catalog.setOwner(owner);

        System.out.println(catalog);
        bookCatalogsService.create(catalog);

        return ResponseCatalog.addSuccess();
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<?> getCatalog(@PathVariable int userId,
                                                          @PathVariable int catalogId) {

        BookCatalog catalog = bookCatalogsService.readById(catalogId);


        Person owner = catalog.getOwner();

        if (owner == null || owner.getId() != userId) {
            return ResponseUser.noFound();
        }

        return ResponseEntity.ok(new BookCatalogResponse(catalog));
    }

    @PreAuthorize("#userId == principal.id")
    @PutMapping("/{catalogId}")
    public ResponseEntity<?> updateCatalog(@PathVariable int userId,
                                           @PathVariable int catalogId,
                                           @RequestBody BookCatalogRequest bookCatalogRequest) {

        if (bookCatalogRequest.isTitleEmpty()) return ResponseCatalog.noContent();

        Person person = peopleService.readById(userId);

        BookCatalog catalog = bookCatalogsService.getByIdAndPerson(catalogId, person);

        catalog.setTitle(bookCatalogRequest.getTitle());
        catalog.setIsPublic(bookCatalogRequest.getIsPublic());
        catalog.setOwner(person);


        return ResponseEntity.ok(new BookCatalogResponse(bookCatalogsService.update(catalog)));

}

    @DeleteMapping("/{catalogId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable String userId,
                                           @PathVariable int catalogId) {

        bookCatalogsService.deleteById(catalogId);

        return ResponseCatalog.deleteSuccess();
    }
}
