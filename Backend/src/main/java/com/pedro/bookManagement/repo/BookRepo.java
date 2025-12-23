package com.pedro.bookManagement.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pedro.bookManagement.model.Book;

@Repository
public interface BookRepo extends JpaRepository<Book, String> {

	Page<Book> findByAuthor(String author, Pageable pageable);
	
	Page<Book> findByGenre(String genre, Pageable pageable);

	Page<Book> findByYearPublished(int yearPublished, Pageable pageable);

	Page<Book> findByAuthorAndGenre(String author, String genre, Pageable pageable);

	Page<Book> findByAuthorAndYearPublished(String author, int yearPublished, Pageable pageable);

	Page<Book> findByGenreAndYearPublished(String genre, int yearPublished, Pageable pageable);

	Page<Book> findByAuthorAndGenreAndYearPublished(String author, String genre, Integer yearPublished, Pageable pageable);

	Optional<Book> findByTitle(String title);

	boolean existsByIsbn(String isbn);

}
