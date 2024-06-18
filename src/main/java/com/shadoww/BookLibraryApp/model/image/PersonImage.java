package com.shadoww.BookLibraryApp.model.image;


import com.shadoww.BookLibraryApp.model.user.Person;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("User")
@Setter
@Getter
@NoArgsConstructor
public class PersonImage extends Image {

    @OneToOne
    @JoinColumn(name = "owner_id")
    private Person owner;
}
