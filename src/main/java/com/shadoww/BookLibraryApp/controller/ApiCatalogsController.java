package com.shadoww.BookLibraryApp.controller;


import com.shadoww.BookLibraryApp.dto.response.BookCatalogResponse;
import com.shadoww.BookLibraryApp.dto.request.BookCatalogRequest;
import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.service.interfaces.BookCatalogService;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import com.shadoww.BookLibraryApp.util.responsers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users/{userId}/catalogs")
public class ApiCatalogsController {

    private final PersonService personService;
    private final BookCatalogService bookCatalogService;

    @Autowired
    public ApiCatalogsController(PersonService personService,
                                 BookCatalogService bookCatalogService) {
        this.personService = personService;
        this.bookCatalogService = bookCatalogService;
    }

    @GetMapping
    public ResponseEntity<?> getBookCatalogsByUser(@PathVariable long userId) {
        Person person = personService
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
    public ResponseEntity<?> addCatalog(@PathVariable long userId,
                                        @RequestBody BookCatalogRequest bookCatalogRequest) {

        if (bookCatalogRequest.isTitleEmpty()) return ResponseCatalog.noContent();

        Person owner = personService.readById(userId);

        BookCatalog catalog = new BookCatalog(bookCatalogRequest);

        catalog.setOwner(owner);

        System.out.println(catalog);
        bookCatalogService.create(catalog);

        return ResponseCatalog.addSuccess();
    }

    @GetMapping("/{catalogId}")
    public ResponseEntity<?> getCatalog(@PathVariable long userId,
                                                          @PathVariable long catalogId) {

        BookCatalog catalog = bookCatalogService.readById(catalogId);


        Person owner = catalog.getOwner();

        if (owner == null || owner.getId() != userId) {
            return ResponseUser.noFound();
        }

        return ResponseEntity.ok(new BookCatalogResponse(catalog));
    }

    @PreAuthorize("#userId == principal.id")
    @PutMapping("/{catalogId}")
    public ResponseEntity<?> updateCatalog(@PathVariable long userId,
                                           @PathVariable long catalogId,
                                           @RequestBody BookCatalogRequest bookCatalogRequest) {

        if (bookCatalogRequest.isTitleEmpty()) return ResponseCatalog.noContent();

        Person person = personService.readById(userId);

        BookCatalog catalog = bookCatalogService.getByIdAndPerson(catalogId, person);

        catalog.setTitle(bookCatalogRequest.getTitle());
        catalog.setIsPublic(bookCatalogRequest.getIsPublic());
        catalog.setOwner(person);


        return ResponseEntity.ok(new BookCatalogResponse(bookCatalogService.update(catalog)));

}

    @DeleteMapping("/{catalogId}")
    public ResponseEntity<?> deleteCatalog(@PathVariable String userId,
                                           @PathVariable long catalogId) {

        bookCatalogService.deleteById(catalogId);

        return ResponseCatalog.deleteSuccess();
    }
}
