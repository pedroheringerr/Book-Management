package com.pedro.bookManagement.dto;

public record BookResponseDTO(
	String isbn,
	String title,
	String author,
	Integer yearPublished,
	String genre
) {}
