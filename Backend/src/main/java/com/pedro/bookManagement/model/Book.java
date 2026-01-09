package com.pedro.bookManagement.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Book {

	@Id
	@NotBlank(message="ISBN is mandatory")
	@Pattern(
		regexp = "^\\d{13}$",
		message = "ISBN must contain 13 digits"
	)
	@Column(length = 13)
	private String isbn;

	@NotBlank(message="Title is mandatory")
	@Column(length = 150)
	private String title;

	@NotBlank(message="Author is mandatory")
	@Column(length = 100)
	private String author;

	@NotNull(message = "Year published is mandatory")
	@Min(value = 1450, message = "Year published must be after the invention of printing")
	@Max(value = 2100, message = "Year published must be a valid year")
	@Column
	private Integer yearPublished;

	@Column(length = 50)
	private String genre;

  @CreationTimestamp
  @Column(updatable = false, name = "created_at")
  private Date createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Date updatedAt;

	public Book() {}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYearPublished() {
		return yearPublished;
	}

	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}
