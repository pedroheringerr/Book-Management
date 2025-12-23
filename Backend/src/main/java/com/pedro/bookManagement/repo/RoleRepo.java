package com.pedro.bookManagement.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedro.bookManagement.model.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String Name);
	
}
