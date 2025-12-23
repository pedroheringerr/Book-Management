package com.pedro.bookManagement.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pedro.bookManagement.exception.ResourceNotFoundException;
import com.pedro.bookManagement.model.Role;
import com.pedro.bookManagement.model.User;
import com.pedro.bookManagement.repo.RoleRepo;
import com.pedro.bookManagement.repo.UserRepo;
import com.pedro.bookManagement.dto.*;

@Service
public class UserService {

	private final UserRepo userRepo;

	private final RoleRepo roleRepo;

	public UserService(UserRepo userRepo, RoleRepo roleRepo) {
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
	}

	private UserResponseDTO mapToDTO(User user) {
    return new UserResponseDTO(
        user.getId(),
        user.getEmail(),
        user.getFirstName(),
        user.getLastName(),
        user.getRoles()
            .stream()
            .map(Role::getName)
            .collect(Collectors.toSet())
    );
	}


	public Page<UserResponseDTO> findAll(Integer page, Integer size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return userRepo.findAll(pageRequest)
					.map(user -> new UserResponseDTO(
						user.getId(),
						user.getEmail(),
						user.getFirstName(),
						user.getLastName(),
						user.getRoles()
							.stream()
							.map(Role::getName)
							.collect(Collectors.toSet())
					));
	}

	public UserResponseDTO findById(Long id) {
		return userRepo.findById(id)
			.map(user -> new UserResponseDTO(
						user.getId(),
						user.getEmail(),
						user.getFirstName(),
						user.getLastName(),
						user.getRoles()
							.stream()
							.map(Role::getName)
							.collect(Collectors.toSet())
					))
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	public UserResponseDTO findByEmail(String email) {
		return userRepo.findByEmail(email)
			.map(user -> new UserResponseDTO(
						user.getId(),
						user.getEmail(),
						user.getFirstName(),
						user.getLastName(),
						user.getRoles()
							.stream()
							.map(Role::getName)
							.collect(Collectors.toSet())
					))
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	public void deleteUser(Long id) {
    if (!userRepo.existsById(id)) {
        throw new ResourceNotFoundException("User not found");
    }
    userRepo.deleteById(id);
	}

	public UserResponseDTO updateUserRoles(Long userId, Set<String> roleNames) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Set<Role> roles = roleNames.stream()
        .map(name -> roleRepo.findByName(name)
            .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name)))
        .collect(Collectors.toSet());

    user.setRoles(roles);

    return mapToDTO(user);
	}
	
}
