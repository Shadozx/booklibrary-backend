package com.shadoww.BookLibraryApp.auth.service;

import com.shadoww.BookLibraryApp.auth.request.AuthRequest;
import com.shadoww.BookLibraryApp.auth.response.AuthResponse;
import com.shadoww.BookLibraryApp.dto.request.users.AuthPersonRequest;
import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final PeopleService peopleService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(PeopleService peopleService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.peopleService = peopleService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public Person register(AuthPersonRequest request) {
        Person newUser = new Person();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        return peopleService.create(newUser);
    }

    public AuthResponse authenticate(AuthRequest request) {
        Person person = peopleService.readByUsername(request.getUsername());


        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(person.getUsername(), person.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (Exception e){
            System.out.println(e);
        }

        var jwt = jwtService.generateToken(person);

        return new AuthResponse(jwt);
    }

}

