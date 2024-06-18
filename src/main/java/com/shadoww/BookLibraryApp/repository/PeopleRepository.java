package com.shadoww.BookLibraryApp.repository;


import com.shadoww.BookLibraryApp.model.user.Person;
import com.shadoww.BookLibraryApp.model.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Long> {
    boolean existsByUsernameIgnoreCase(String username);

    Page<Person> findByUsernameContainingIgnoreCase(Pageable page, String username);

    Optional<Person> findByUsername(String username);
    Optional<Person> findByEmail(String email);

    boolean existsByRole(Role role);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
