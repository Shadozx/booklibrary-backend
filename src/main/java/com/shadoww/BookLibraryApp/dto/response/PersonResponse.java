package com.shadoww.BookLibraryApp.dto.response;

import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.models.user.Role;
import com.shadoww.BookLibraryApp.models.user.Theme;
import lombok.Value;

@Value
public class PersonResponse {

    long id;

    String username;

    String roleName;

    String theme;

    String userImageUrl;

    public PersonResponse(Person person) {
        this.id = person.getId();
        this.username = person.getUsername();
        this.roleName = person.getRole() == null ? Role.USER.name() : person.getRole().name();
        if (person.getPersonImage() != null) {
            this.userImageUrl = person.getPersonImage().getUrl();
        } else {
            this.userImageUrl = null;
        }
        this.theme = person.getTheme() == null ? Theme.LIGHT.name() : person.getTheme().name();

        System.out.println();
    }

}
