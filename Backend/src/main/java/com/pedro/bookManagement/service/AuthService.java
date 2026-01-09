package com.pedro.bookManagement.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pedro.bookManagement.dto.JwtResponse;
import com.pedro.bookManagement.dto.UserLoginRequest;
import com.pedro.bookManagement.dto.UserRegisterRequest;
import com.pedro.bookManagement.dto.UserResponseDTO;
import com.pedro.bookManagement.exception.DuplicateResourceException;
import com.pedro.bookManagement.exception.ResourceNotFoundException;
import com.pedro.bookManagement.model.Role;
import com.pedro.bookManagement.model.User;
import com.pedro.bookManagement.repo.RoleRepo;
import com.pedro.bookManagement.repo.UserRepo;
import com.pedro.bookManagement.security.JwtUtil;

@Service
public class AuthService {

	private final UserRepo userRepo;

	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	private final JwtUtil jwtUtil;

	private final RoleRepo roleRepo;

	public AuthService(
			UserRepo userRepo,
			AuthenticationManager authenticationManager, 
			PasswordEncoder passwordEncoder,
			JwtUtil jwtUtil,
			RoleRepo roleRepo
	) {
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.roleRepo = roleRepo;
	}
	
	public JwtResponse authenticateUser(UserLoginRequest dto) {
		if (!userRepo.existsByEmail(dto.email())) {
			throw new ResourceNotFoundException("User with this email doesn't exist");
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
					dto.email(), 
					dto.password()
				)
		);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		User user = userRepo.findByEmail(userDetails.getUsername())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Set<String> roles = user.getRoles()
				.stream()
				.map(Role::getName)
				.collect(Collectors.toSet());

		return new JwtResponse(jwtUtil.generateToken(user.getEmail(), roles));

	}

	public UserResponseDTO registerUser(UserRegisterRequest dto) {
		if (userRepo.existsByEmail(dto.email())) {
			throw new DuplicateResourceException("User with this email alredy exists");
		}
		User user = new User();
    user.setEmail(dto.email());
    user.setFirstName(dto.firstName());
    user.setLastName(dto.lastName());
    user.setPassword(passwordEncoder.encode(dto.password()));

		Role readerRole = roleRepo.findByName("READER")
        .orElseThrow(() -> new RuntimeException("Role READER not found"));

    user.getRoles().add(readerRole);

		User saved = userRepo.save(user);

		return new UserResponseDTO(saved.getId(), saved.getEmail(), saved.getFirstName(), saved.getLastName(), 
				saved.getRoles()
					.stream()
					.map(Role::getName)
					.collect(Collectors.toSet())
					);

	}

}
