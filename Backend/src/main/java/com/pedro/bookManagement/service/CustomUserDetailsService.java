package com.pedro.bookManagement.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pedro.bookManagement.exception.ResourceNotFoundException;
import com.pedro.bookManagement.model.User;
import com.pedro.bookManagement.repo.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepo userRepo;

	public CustomUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(email)
			.orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));
		List<GrantedAuthority> authorities = user.getRoles().stream()
			.map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.getName()))
			.toList();
		return new org.springframework.security.core.userdetails.User(
        user.getEmail(),
        user.getPassword(),
        authorities
    );
	}
	
}
