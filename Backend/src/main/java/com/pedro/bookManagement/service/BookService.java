package com.pedro.bookManagement.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.pedro.bookManagement.exception.DuplicateResourceException;
import com.pedro.bookManagement.exception.ResourceNotFoundException;
import com.pedro.bookManagement.model.Book;
import com.pedro.bookManagement.repo.BookRepo;
import com.pedro.bookManagement.dto.BookResponseDTO;

@Service
public class BookService {

	private final BookRepo bookRepo;

	public BookService(BookRepo bookRepo) {
		this.bookRepo = bookRepo;
	}
	
	@Cacheable(cacheNames="book", key="#isbn")
	public Book findByIsbn(String isbn) {
		return bookRepo.findById(isbn).
			orElseThrow(() -> new ResourceNotFoundException("Book not found"));
	}

	@Cacheable(cacheNames="book")
	public Book findByTitle(String title) {
		return bookRepo.findByTitle(title)
			.orElseThrow(() -> new ResourceNotFoundException("Book not found"));
	}

	@Cacheable(cacheNames="books", key="{ #genre, #author, #yearPublished, #search, #page, #size }")
	public Page<BookResponseDTO> searchBooks(
			String genre,
			String author,
			Integer yearPublished,
			String search,
			Integer page,
			Integer size
			) {
		PageRequest pageRequest = PageRequest.of(
				page,
				size
				);
		return this.bookRepo.
			findByFilteredAndSearched(
				genre,
				author, 
				yearPublished,
				search, 
				pageRequest
				).map(book -> new BookResponseDTO(
						book.getIsbn(),
						book.getTitle(), 
						book.getAuthor(),
						book.getYearPublished(),
						book.getGenre()));
			}
	
	@CacheEvict(cacheNames="books", allEntries=true)
	public Book saveBook(Book book) {
		if (bookRepo.existsByIsbn(book.getIsbn())) {
			throw new DuplicateResourceException("ISBN already exists");
		}
		return bookRepo.save(book);
	}

	@Caching(put = @CachePut(cacheNames="book", key="#isbn"), evict = @CacheEvict(cacheNames="books", allEntries=true))
	public Book updateBook(String isbn, Book book) {
		Book updatedBook = bookRepo.findById(isbn).get(); 
		updatedBook.setTitle(book.getTitle());
		updatedBook.setGenre(book.getGenre());
		updatedBook.setAuthor(book.getAuthor());
		updatedBook.setYearPublished(book.getYearPublished());
		return bookRepo.save(updatedBook);
	}

	@Caching(evict = {
		@CacheEvict(cacheNames="books", allEntries=true),
		@CacheEvict(cacheNames="book", key="#isbn"),
	})
	public void deleteBook(String isbn) {
		if (!bookRepo.existsByIsbn(isbn)) {
			throw new ResourceNotFoundException("Book not found");
		}
		bookRepo.deleteById(isbn);
	}

}
