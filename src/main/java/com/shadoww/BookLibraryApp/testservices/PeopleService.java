package com.shadoww.BookLibraryApp.testservices;

import com.shadoww.BookLibraryApp.models.user.Person;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface PeopleService extends UserDetailsService {

    List<Person> getAll();

    Page<Person> getALl(int page);

    Person readByUsername(String username);

    Page<Person> readByUsername(String username, int page);

    Person readById(int id);

    boolean existByUsername(String username);

    boolean existSUPERADMIN();

    Person create(Person person);

    Person update(Person person);

    void deleteById(int id);
}
