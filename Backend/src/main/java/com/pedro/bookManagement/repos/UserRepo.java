package com.pedro.bookManagement.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pedro.bookManagement.models.User;

public interface UserRepo extends JpaRepository<User, Long> {

	
}
