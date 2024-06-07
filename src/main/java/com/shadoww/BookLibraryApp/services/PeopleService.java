package com.shadoww.BookLibraryApp.services;

import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.models.user.Role;
import com.shadoww.BookLibraryApp.models.user.Theme;
import com.shadoww.BookLibraryApp.repositories.PeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAll() {
        return peopleRepository.findAll();
    }

    public Page<Person> getALl(int page) {
        return peopleRepository.findAll(PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "username")));
    }

    public Person readByUsername(String username) {

        return peopleRepository
                .findByUsername(username)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("Користувача з нікнеймом %s не існує", username)));
    }


    public Page<Person> readByUsername(String username, int page) {
        return peopleRepository.findByUsernameContainingIgnoreCase(PageRequest.of(page, 20, Sort.by(Sort.Direction.ASC, "username")), username);
    }

    public Person readById(int id) {
        return peopleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Такого користувача не існує"));
    }


    public boolean existByUsername(String username) {
        return peopleRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean existSUPERADMIN() {
        return peopleRepository.existsByRole(Role.SUPER_ADMIN);
    }

    @Transactional
    public Person create(Person person) {

        person.setRole(Role.USER);
        person.setTheme(Theme.LIGHT);

        person.setPassword(passwordEncoder.encode(person.getPassword()));

        return save(person);

    }

    @Transactional
    public Person update(Person person) {


        return save(person);
    }

    @Transactional
    public Person save(Person person) {
        return peopleRepository.save(person);
    }

    @Transactional
    public void deleteById(int id) {
        delete(readById(id));
    }

    @Transactional
    void delete(Person person) {
        peopleRepository.delete(person);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return readByUsername(username);
        }catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}

