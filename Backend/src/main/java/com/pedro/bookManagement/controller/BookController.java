package com.pedro.bookManagement.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pedro.bookManagement.dto.BookResponseDTO;
import com.pedro.bookManagement.model.Book;
import com.pedro.bookManagement.service.BookService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;


@RequestMapping("/api/books")
@RestController
@Validated
public class BookController {
		
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
		public ResponseEntity<Page<BookResponseDTO>> getBooksByFilter(
			@RequestParam(required = false) String genre,
			@RequestParam(required = false) String author,
			@RequestParam(required = false)
			@PositiveOrZero
			Integer yearPublished,
			@RequestParam(required = false)	String search,

			@RequestParam(value = "page", required = false, defaultValue = "0")
			@Min(value = 0, message = "Page must be 0 or greater")
			@PositiveOrZero(message = "Page must be 0 or greater")
			Integer page,

			@RequestParam(value = "size", required = false, defaultValue = "9")
			@Min(value = 1, message = "Size must be at least 1")
			@Max(value = 50, message = "Size must be at most 50")
			Integer size
			) {
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(bookService.searchBooks(genre, author, yearPublished, search, page, size));
			}

	@GetMapping("/{isbn}")
	public ResponseEntity<Book> getBookByIsbn(@PathVariable String isbn) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(bookService.findByIsbn(isbn));
	}

	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(bookService.saveBook(book));
	}

	@PutMapping("/{isbn}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Book> updateBook(@PathVariable String isbn, @Valid @RequestBody Book book) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(bookService.updateBook(isbn, book));
	}

	@DeleteMapping("/{isbn}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Void> deleteBook(@PathVariable String isbn) {
		bookService.deleteBook(isbn);
		return ResponseEntity.noContent().build();
	}
}
