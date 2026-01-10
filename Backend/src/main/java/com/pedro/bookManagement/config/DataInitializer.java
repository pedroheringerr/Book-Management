package com.pedro.bookManagement.config;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pedro.bookManagement.model.Book;
import com.pedro.bookManagement.model.Role;
import com.pedro.bookManagement.model.User; 
import com.pedro.bookManagement.repo.BookRepo;
import com.pedro.bookManagement.repo.RoleRepo;
import com.pedro.bookManagement.repo.UserRepo;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initData(
      RoleRepo roleRepo,
      UserRepo userRepo,
      BookRepo bookRepo,
      PasswordEncoder passwordEncoder
  ) {
    return args -> {

      Role reader = createRoleIfNotExists(roleRepo, "READER");
      Role user   = createRoleIfNotExists(roleRepo, "USER");
      Role admin  = createRoleIfNotExists(roleRepo, "ADMIN");

      createAdminIfNotExists(userRepo, passwordEncoder, admin, user);

      createBooksIfNotExists(bookRepo);
    };
  }

  private Role createRoleIfNotExists(RoleRepo repo, String name) {
    return repo.findByName(name)
        .orElseGet(() -> repo.save(new Role(name)));
  }

  private void createAdminIfNotExists(
      UserRepo userRepo,
      PasswordEncoder encoder,
      Role adminRole,
			Role userRole
  ) {
    String email = "admin@system.com";

    if (userRepo.findByEmail(email).isPresent()) {
      return;
    }

    User admin = new User();
    admin.setEmail(email);
    admin.setPassword(encoder.encode("admin123"));
    admin.setRoles(Set.of(adminRole, userRole));

    userRepo.save(admin);
  }

	private void createBookIfNotExists(
		BookRepo bookRepo,
		String isbn,
		String title,
		String author,
		int yearPublished,
		String genre
	) {
		if (bookRepo.existsById(isbn)) {
			return;
		}

		Book book = new Book();
		book.setIsbn(isbn);
		book.setTitle(title);
		book.setAuthor(author);
		book.setYearPublished(yearPublished);
		book.setGenre(genre);

		bookRepo.save(book);
	}

	
	private void createBooksIfNotExists(BookRepo bookRepo) {

		createBookIfNotExists(
			bookRepo,
			"9780140449150",
			"The Prince",
			"Niccolò Machiavelli",
			1532,
			"Political Philosophy"
		);

		createBookIfNotExists(
			bookRepo,
			"9780451524935",
			"1984",
			"George Orwell",
			1949,
			"Dystopian"
		);

		createBookIfNotExists(
			bookRepo,
			"9780451526342",
			"Animal Farm",
			"George Orwell",
			1945,
			"Political Satire"
		);

		createBookIfNotExists(
			bookRepo,
			"9780547928227",
			"The Hobbit",
			"J.R.R. Tolkien",
			1937,
			"Fantasy"
		);

		createBookIfNotExists(
			bookRepo,
			"9780747532699",
			"Harry Potter and the Philosopher's Stone",
			"J.K. Rowling",
			1997,
			"Fantasy"
		);

		createBookIfNotExists(
			bookRepo,
			"9780747538493",
			"Harry Potter and the Chamber of Secrets",
			"J.K. Rowling",
			1998,
			"Fantasy"
		);

		createBookIfNotExists(
			bookRepo,
			"9780618640157",
			"The Lord of the Rings",
			"J.R.R. Tolkien",
			1954,
			"Fantasy"
		);

		createBookIfNotExists(
			bookRepo,
			"9780140449136",
			"Crime and Punishment",
			"Fyodor Dostoevsky",
			1866,
			"Classic Literature"
		);

		createBookIfNotExists(
			bookRepo,
			"9780142437223",
			"The Divine Comedy",
			"Dante Alighieri",
			1472,
			"Epic Poetry"
		);

		createBookIfNotExists(
			bookRepo,
			"9780060934347",
			"Don Quixote",
			"Miguel de Cervantes",
			1605,
			"Classic Literature"
		);
	}
}
