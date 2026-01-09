package com.pedro.bookManagement.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	@Query("""
  SELECT b FROM Book b
  WHERE (:genre IS NULL OR b.genre = :genre)
    AND (:author IS NULL OR b.author = :author)
    AND (:yearPublished IS NULL OR b.yearPublished = :yearPublished)
    AND (
      :search IS NULL OR
      LOWER(b.title) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%')) OR
      LOWER(b.author) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))
    )
	""")
	Page<Book> findByFilteredAndSearched(
			@Param("genre") String genre,
			@Param("author") String author,
			@Param("yearPublished") Integer yearPublished,
			@Param("search") String search,
			Pageable pageable
			);

	boolean existsByIsbn(String isbn);

}
