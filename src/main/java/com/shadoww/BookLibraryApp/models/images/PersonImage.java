package com.shadoww.BookLibraryApp.models.images;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shadoww.BookLibraryApp.models.user.Person;
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


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "owner", nullable = true)
    private Person owner;

}
