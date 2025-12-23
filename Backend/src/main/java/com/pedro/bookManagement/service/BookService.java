package com.pedro.bookManagement.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pedro.bookManagement.exception.DuplicateResourceException;
import com.pedro.bookManagement.exception.ResourceNotFoundException;
import com.pedro.bookManagement.model.Book;
import com.pedro.bookManagement.repo.BookRepo;

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

	@Cacheable(cacheNames="book", key="#title")
	public Book findByTitle(String title) {
		return bookRepo.findByTitle(title)
			.orElseThrow(() -> new ResourceNotFoundException("Book not found"));
	}

	@Cacheable(cacheNames="books", key="{ #genre, #author, #yearPublished, #page, #size }")
	public Page<Book> findByFilter(String genre, String author, Integer yearPublished, Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(
				page,
				size
				);
		if (genre != null && author != null && yearPublished != null) {
			return bookRepo.findByAuthorAndGenreAndYearPublished(author, genre, yearPublished, pageRequest);
		}
		if (genre != null && author != null) {
			return bookRepo.findByAuthorAndGenre(author, genre, pageRequest);
		}
		if (genre != null && yearPublished != null) {
			return bookRepo.findByGenreAndYearPublished(genre, yearPublished, pageRequest);
		}
		if (author != null && yearPublished != null) {
			return bookRepo.findByAuthorAndYearPublished(author, yearPublished, pageRequest);
		}
		if (author != null) {
			return bookRepo.findByAuthor(author, pageRequest);
		}
		if (genre != null) {
			return bookRepo.findByGenre(genre, pageRequest);
		}
		if (yearPublished != null) {
			return bookRepo.findByYearPublished(yearPublished, pageRequest);
		}
		return bookRepo.findAll(pageRequest);
	}
	
	@CacheEvict(cacheNames="books", allEntries=true)
	public Book saveBook(Book book) {
		if (bookRepo.existsByIsbn(book.getIsbn())) {
			throw new DuplicateResourceException("ISBN already exists");
		}
		return bookRepo.save(book);
	}

	@Caching(put = { @CachePut(cacheNames="book", key="#isbn"), @CachePut(cacheNames="book", key="#title")}, evict = @CacheEvict(cacheNames="books", allEntries=true))
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
		@CacheEvict(cacheNames="book", key="#title")
	})
	public void deleteBook(String isbn) {
		if (!bookRepo.existsByIsbn(isbn)) {
			throw new ResourceNotFoundException("Book not found");
		}
		bookRepo.deleteById(isbn);
	}

}
