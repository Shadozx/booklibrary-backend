package com.shadoww.BookLibraryApp.controller.user;


import com.shadoww.BookLibraryApp.dto.request.BookRatingRequest;
import com.shadoww.BookLibraryApp.dto.request.user.PersonRequest;
import com.shadoww.BookLibraryApp.dto.response.BookMarkResponse;
import com.shadoww.BookLibraryApp.dto.response.BookRatingResponse;
import com.shadoww.BookLibraryApp.dto.response.PersonResponse;
import com.shadoww.BookLibraryApp.model.Book;
import com.shadoww.BookLibraryApp.model.BookRating;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.model.user.Theme;
import com.shadoww.BookLibraryApp.security.AuthUser;
import com.shadoww.BookLibraryApp.service.interfaces.BookMarkService;
import com.shadoww.BookLibraryApp.service.interfaces.BookRatingService;
import com.shadoww.BookLibraryApp.service.interfaces.BookService;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
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

    private final PersonService personService;

    private final BookMarkService bookMarkService;
    private final BookService bookService;

    private final BookRatingService bookRatingService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApiUsersController(PersonService personService,
                              BookMarkService bookMarkService,
                              BookService bookService,
                              BookRatingService bookRatingService,
                              PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.bookMarkService = bookMarkService;
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(personService.getAll()
                .stream()
                .map(PersonResponse::new)
                .toList());
    }

    @GetMapping("/{userId}/bookmarks")
    public ResponseEntity<?> getBookMarkByBookAndUser(@PathVariable long userId,
                                                      @RequestParam long bookId) {

        Book book = bookService.readById(bookId);

        Person owner = personService.readById(userId);

        System.out.println("Get bookmark by book and owner");

        System.out.println(owner);

        System.out.println(book);

        return ResponseEntity.ok(
                new BookMarkResponse(
                        bookMarkService.getByBookAndOwner(book, owner))
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
    public ResponseEntity<?> getUser(@PathVariable long id) {
        Person person = personService.readById(id);

        return ResponseEntity.ok(new PersonResponse(person));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable long userId,
                                            @RequestBody PersonRequest form) {

        System.out.println("Form: " + form);

        Person person = personService.readById(userId);


        if (form.isEmpty()) return ResponseUser.noContent();

        if (!form.isUsernameEmpty()) person.setUsername(form.getUsername());

        if (!form.isPasswordEmpty()) person.setPassword(passwordEncoder.encode(form.getPassword()));

        if (!form.isRoleEmpty()) person.setRole(form.getRole());

        System.out.println("Updated person: " + person);



//        return ResponseUser.updateSuccess();

        return ResponseEntity.ok(new PersonResponse(personService.create(person)));

    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deletePerson(@PathVariable long userId) {

        personService.deleteById(userId);

        return ResponseUser.deleteSuccess();
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') || #userId == principal.id")
    @PostMapping("/{userId}/theme")
    public ResponseEntity<?> changeTheme(@PathVariable long userId) {
        Person person = personService.readById(userId);

        Theme theme = person.getTheme();

        person.setTheme(Theme.next(theme));

        personService.update(person);

        return ResponseEntity.ok().body("Тема профіля користувача була успішно оновлена");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        Person person = authUser.getPerson();

        return ResponseEntity.ok(new PersonResponse(person));
    }


    @GetMapping("/{userId}/ratings")
    public ResponseEntity<?> getBookRating(@PathVariable long userId,
                                           @RequestParam long bookId) {

        System.out.println(userId);
        System.out.println(bookId);
        Person owner = personService.readById(userId);
        Book book = bookService.readById(bookId);

        return ResponseEntity.ok(new BookRatingResponse(bookRatingService.getBookRatingByOwnerAndBook(owner, book)));
    }
    @PostMapping("/{userId}/ratings")
    public ResponseEntity<?> createBookRating(@PathVariable long userId,
                                              @RequestBody BookRatingRequest request) {

        Person owner = personService.readById(userId);

        BookRating rating = new BookRating();
        rating.setOwner(owner);
        rating.setRating(request.getRating());
        rating.setBook(bookService.readById(request.getBookId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(new BookRatingResponse(bookRatingService.create(rating)));
    }

    @PutMapping("/{userId}/ratings/{ratingId}")
    public ResponseEntity<?> updateBookRating(@PathVariable long userId,
                                              @PathVariable long ratingId,
                                              @RequestBody BookRatingRequest request) {

        BookRating rating = bookRatingService.readById(ratingId);

        rating.setRating(request.getRating());

        return ResponseEntity.status(HttpStatus.OK).body(new BookRatingResponse(bookRatingService.update(rating)));
    }

    @DeleteMapping("/{userId}/ratings/{ratingId}")
    public ResponseEntity<?> deleteBookRating(@PathVariable long userId,
                                              @PathVariable long ratingId) {

        bookRatingService.deleteById(ratingId);

        return ResponseEntity.ok().build();
    }
}
