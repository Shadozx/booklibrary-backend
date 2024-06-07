package com.shadoww.BookLibraryApp.repositories;


import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.models.user.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    boolean existsByUsernameIgnoreCase(String username);

    Page<Person> findByUsernameContainingIgnoreCase(Pageable page, String username);

    Optional<Person> findByUsername(String username);

    boolean existsByRole(Role role);
}
