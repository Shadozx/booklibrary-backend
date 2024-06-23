package com.shadoww.BookLibraryApp.model.comment;

import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "comment_type")
@Getter
@Setter
@NoArgsConstructor
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Size(min = 1, max = 200, message = "Comment must have from 1 to 200 letters")
    @NotBlank(message = "Comment cannot be empty")
    @Column(nullable = false, length = 200)
    private String text;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

}

