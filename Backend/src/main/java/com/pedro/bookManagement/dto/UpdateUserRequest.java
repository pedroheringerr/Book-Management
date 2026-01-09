package com.pedro.bookManagement.dto;

import java.util.Set;

public record UpdateUserRequest(
		String email,
		String firstName,
		String lastName,
		Set<String> roles
) {}
