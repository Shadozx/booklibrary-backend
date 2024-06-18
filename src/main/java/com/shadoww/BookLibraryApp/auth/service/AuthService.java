package com.shadoww.BookLibraryApp.auth.service;

import com.shadoww.BookLibraryApp.auth.request.AuthRequest;
import com.shadoww.BookLibraryApp.auth.response.AuthResponse;
import com.shadoww.BookLibraryApp.dto.request.user.AuthPersonRequest;
import com.shadoww.BookLibraryApp.exception.ValueAlreadyExistsException;
import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.security.AuthUser;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PersonService personService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(PersonService personService, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.personService = personService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }


    public Person register(AuthPersonRequest request) {

        if (personService.existsByEmail(request.getEmail())) {
            throw new ValueAlreadyExistsException("Email вже використовується");
        }

        if (personService.existsByUsername(request.getUsername())) {
            throw new ValueAlreadyExistsException("Нікнейм вже використовується");
        }

        Person newUser = new Person();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        return personService.create(newUser);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Person person = personService.readByEmail(request.getEmail());


        System.out.println(person);
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());


        authenticationManager.authenticate(authenticationToken);

        return new AuthResponse(jwtService.generateToken(new AuthUser(person)));
    }

}

