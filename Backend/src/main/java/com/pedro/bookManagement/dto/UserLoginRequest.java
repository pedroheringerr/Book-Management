package com.pedro.bookManagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(

	@NotBlank @Email String email,
	@NotBlank @Min(6) String password

) {}
