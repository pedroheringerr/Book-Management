package com.pedro.bookManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.bookManagement.dto.UserLoginRequest;
import com.pedro.bookManagement.dto.UserRegisterRequest;
import com.pedro.bookManagement.dto.UserResponseDTO;
import com.pedro.bookManagement.service.AuthService;

@RequestMapping("/api/auth")
@RestController
public class AuthController {

	private final AuthService authService;


	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/signin")
	public String authenticateUser(@RequestBody UserLoginRequest dto) {
		return authService.authenticateUser(dto);
	}

	@PostMapping("/signup")
	public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRegisterRequest dto) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(authService.registerUser(dto));
	}
	
}
