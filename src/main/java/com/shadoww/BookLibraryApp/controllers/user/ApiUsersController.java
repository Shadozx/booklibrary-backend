package com.shadoww.BookLibraryApp.controllers.user;


import com.shadoww.BookLibraryApp.dto.request.BookRatingRequest;
import com.shadoww.BookLibraryApp.dto.request.users.PersonRequest;
import com.shadoww.BookLibraryApp.dto.response.BookMarkResponse;
import com.shadoww.BookLibraryApp.dto.response.BookRatingResponse;
import com.shadoww.BookLibraryApp.dto.response.PersonResponse;
import com.shadoww.BookLibraryApp.models.Book;
import com.shadoww.BookLibraryApp.models.BookRating;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.models.user.Theme;
import com.shadoww.BookLibraryApp.services.BookMarksService;
import com.shadoww.BookLibraryApp.services.BookRatingsService;
import com.shadoww.BookLibraryApp.services.BooksService;
import com.shadoww.BookLibraryApp.services.PeopleService;
import com.shadoww.BookLibraryApp.util.responsers.ResponseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


//@CrossOrigin(origins = "http://localhost:5173")
@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/users")
public class ApiUsersController {

    private final PeopleService peopleService;

    private final BookMarksService bookMarksService;
    private final BooksService booksService;

    private final BookRatingsService bookRatingsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiUsersController(PeopleService peopleService,
                              BookMarksService bookMarksService,
                              BooksService booksService,
                              BookRatingsService bookRatingsService,
                              PasswordEncoder passwordEncoder) {
        this.peopleService = peopleService;
        this.bookMarksService = bookMarksService;
        this.booksService = booksService;
        this.bookRatingsService = bookRatingsService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(peopleService.getAll()
                .stream()
                .map(PersonResponse::new)
                .toList());
    }

    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<?> getBookMarkByBookAndUser(@PathVariable int userId,
                                                      @RequestParam int bookId) {

        Book book = booksService.readById(bookId);

        Person owner = peopleService.readById(userId);

        System.out.println("Get bookmark by book and owner");

        System.out.println(owner);

        System.out.println(book);

        return ResponseEntity.ok(
                new BookMarkResponse(
                        bookMarksService.findByBookAndOwner(book, owner))
        );
    }

/*
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody PersonRequest form) {

        if (form.isEmpty()) return ResponseUser.noContent("Форма для реєстрації пуста");

        if (form.isUsernameEmpty()) return ResponseUser.noContent("Імя не може бути пустим");
        if (form.isPasswordEmpty()) return ResponseUser.noContent("Пароль не може бути пустим");

        if (peopleService.existByUsername(form.getUsername())) return ResponseUser.exist();

        Person person = new Person();

        person.setUsername(form.getUsername());
        person.setPassword(form.getPassword());

        peopleService.create(person);

        return ResponseUser.addSuccess();
    }
*/

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable int id) {
        Person person = peopleService.readById(id);

        return ResponseEntity.ok(new PersonResponse(person));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable int userId,
                                            @RequestBody PersonRequest form) {

        System.out.println("Form: " + form);

        Person person = peopleService.readById(userId);


        if (form.isEmpty()) return ResponseUser.noContent();

        if (!form.isUsernameEmpty()) person.setUsername(form.getUsername());

        if (!form.isPasswordEmpty()) person.setPassword(passwordEncoder.encode(form.getPassword()));

        if (!form.isRoleEmpty()) person.setRole(form.getRole());

        System.out.println("Updated person: " + person);



//        return ResponseUser.updateSuccess();

        return ResponseEntity.ok(new PersonResponse(peopleService.save(person)));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deletePerson(@PathVariable int userId) {

        peopleService.deleteById(userId);

        return ResponseUser.deleteSuccess();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PostMapping("/{userId}/theme")
    public ResponseEntity<?> changeTheme(@PathVariable int userId) {
        Person person = peopleService.readById(userId);

        Theme theme = person.getTheme();

        person.setTheme(Theme.next(theme));

        peopleService.update(person);

        return ResponseEntity.ok().body("Тема профіля користувача була успішно оновлена");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        Person person = (Person) authentication.getPrincipal();

        return ResponseEntity.ok(new PersonResponse(person));
    }


    @GetMapping("/{userId}/ratings")
    public ResponseEntity<?> getBookRating(@PathVariable int userId,
                                           @RequestParam int bookId) {

        System.out.println(userId);
        System.out.println(bookId);
        Person owner = peopleService.readById(userId);
        Book book = booksService.readById(bookId);

        return ResponseEntity.ok(new BookRatingResponse(bookRatingsService.findByOwnerAndBook(owner, book)));
    }
    @PostMapping("/{userId}/ratings")
    public ResponseEntity<?> createBookRating(@PathVariable int userId,
                                              @RequestBody BookRatingRequest request) {

        Person owner = peopleService.readById(userId);

        BookRating rating = new BookRating();
        rating.setOwner(owner);
        rating.setRating(request.getRating());
        rating.setBook(booksService.readById(request.getBookId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookRatingResponse(bookRatingsService.create(rating)));
    }

    @PutMapping("/{userId}/ratings/{ratingId}")
    public ResponseEntity<?> updateBookRating(@PathVariable int userId,
                                              @PathVariable int ratingId,
                                              @RequestBody BookRatingRequest request) {

        BookRating rating = bookRatingsService.readById(ratingId);

        rating.setRating(request.getRating());

        return ResponseEntity.status(HttpStatus.OK).body(new BookRatingResponse(bookRatingsService.update(rating)));
    }

    @DeleteMapping("/{userId}/ratings/{ratingId}")
    public ResponseEntity<?> deleteBookRating(@PathVariable int userId,
                                              @PathVariable int ratingId) {

        bookRatingsService.deleteById(ratingId);

        return ResponseEntity.ok().build();
    }
}
