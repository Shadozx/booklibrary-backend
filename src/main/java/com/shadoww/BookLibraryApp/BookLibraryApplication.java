package com.shadoww.BookLibraryApp;

import com.shadoww.BookLibraryApp.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookLibraryApplication implements CommandLineRunner {

	private final PersonService personService;

	@Autowired
	public BookLibraryApplication(PersonService personService) {
		this.personService = personService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookLibraryApplication.class, args);
	}

	@Override
	public void run(String... args) {

		if (!personService.existSUPERADMIN()) {
			personService.createSUPERADMIN();
		}
	}
}
