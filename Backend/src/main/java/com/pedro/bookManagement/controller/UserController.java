package com.pedro.bookManagement.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pedro.bookManagement.model.User;
import com.pedro.bookManagement.service.UserService;
import com.pedro.bookManagement.dto.*;

import jakarta.validation.Valid;

@RequestMapping("/api/user")
@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserResponseDTO>> getUsers(
		@RequestParam(required = false, defaultValue = "0") Integer page,
		@RequestParam(required = false, defaultValue = "5") Integer size
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(userService.findAll(page, size));
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserResponseDTO> getUserById(
		@PathVariable Long id
	) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(userService.findById(id));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity
			.noContent()
			.build();
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public UserResponseDTO updateRoles(
			@PathVariable Long id,
			@RequestBody UpdateUserRequest request
			) {
		return userService.updateUserRoles(id, request.email(), request.firstName(), request.lastName(), request.roles());
			}

}
