package com.pedro.bookManagement.dto;

import java.util.Set;

public record UserResponseDTO(

	Long id,
  String email,
  String firstName,
	String lastName,
	Set<String> roles

) {}
