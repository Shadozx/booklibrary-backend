package com.shadoww.BookLibraryApp;

import com.shadoww.BookLibraryApp.models.user.Person;
import com.shadoww.BookLibraryApp.models.user.Role;
import com.shadoww.BookLibraryApp.models.user.Theme;
import com.shadoww.BookLibraryApp.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookLibraryAppApplication implements CommandLineRunner {

//	private final PeopleService peopleService;
//
//	@Autowired
//	public BookLibraryAppApplication(PeopleService peopleService) {
//		this.peopleService = peopleService;
//	}

	public static void main(String[] args) {
		SpringApplication.run(BookLibraryAppApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		if (!peopleService.existSUPERADMIN()) {
//			Person person = new Person();
//
//			person.setUsername("super_admin");
//			person.setPassword("super_admin");
//			person.setRole(Role.SUPER_ADMIN);
//			person.setTheme(Theme.DARK);
//			peopleService.save(person);
//		}
	}
}
