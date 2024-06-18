package com.shadoww.BookLibraryApp.model.user;


import com.shadoww.BookLibraryApp.model.BookCatalog;
import com.shadoww.BookLibraryApp.model.BookRating;
import com.shadoww.BookLibraryApp.model.comment.Comment;
import com.shadoww.BookLibraryApp.model.image.PersonImage;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
//@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username can not be empty")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password cannot be empty")
    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Email address must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    // default role is user
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    // default user's theme is light
    @Enumerated(EnumType.STRING)
    private Theme theme = Theme.LIGHT;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<BookCatalog> catalogs;

    @OneToOne
    @JoinColumn(name = "image_id")
    @Cascade({CascadeType.REMOVE, CascadeType.DELETE})
    private PersonImage personImage;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<BookRating> bookRatings;

    @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = FetchType.LAZY)
    @Cascade({CascadeType.DELETE, CascadeType.REMOVE})
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
                Objects.equals(username, person.username) &&
                Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

}
