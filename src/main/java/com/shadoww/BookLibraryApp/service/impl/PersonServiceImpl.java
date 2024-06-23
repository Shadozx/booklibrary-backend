package com.shadoww.BookLibraryApp.service.impl;

import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.model.user.Role;
import com.shadoww.BookLibraryApp.model.user.Theme;
import com.shadoww.BookLibraryApp.repository.PeopleRepository;
import com.shadoww.BookLibraryApp.security.AuthUser;
import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Person create(Person person) {

        checkIsPersonNull(person);

        person.setPassword(passwordEncoder.encode(person.getPassword()));

        return save(person);
    }

    @Override
    @Transactional
    public Person createSUPERADMIN() {
        Person person = new Person();

        person.setUsername("super_admin");
        person.setPassword(passwordEncoder.encode("super_admin"));
        person.setEmail("superadmin@gmail.com");
        person.setRole(Role.SUPER_ADMIN);
        person.setTheme(Theme.DARK);

        checkIsPersonNull(person);

        return save(person);
    }

    @Override
    public Person readByUsername(String username) {

        return peopleRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Користувача з нікнеймом %s не існує", username)));
    }

    @Override
    public Person readByEmail(String email) {

        return peopleRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Користувача з таким email %s не існує", email)));
    }

    @Override
    public Person readById(Long id) {
        return peopleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Такого користувача не існує"));
    }

    @Override
    public boolean existsById(Long id) {
        return peopleRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return peopleRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return peopleRepository.existsByUsername(username);
    }

    @Override
    public boolean existSUPERADMIN() {
        return peopleRepository.existsByRole(Role.SUPER_ADMIN);
    }


    @Override
    @Transactional
    public Person update(Person person) {

        checkIsPersonNull(person);

        readById(person.getId());

        return save(person);
    }

    @Transactional
    public void deleteById(Long id) {
        delete(readById(id));
    }

    @Override
    public long count() {
        return peopleRepository.count();
    }

    @Override
    public List<Person> getAll() {
        return peopleRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new AuthUser(readByEmail(username));
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Transactional
    Person save(Person person) {
        return peopleRepository.save(person);
    }

    @Transactional
    void delete(Person person) {
        peopleRepository.delete(person);
    }

    private void checkIsPersonNull(Person person) {
        if (person == null) {
            throw new NullPointerException("Користувач не може бути пустим");
        }
    }
}

