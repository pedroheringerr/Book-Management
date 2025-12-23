package com.pedro.bookManagement.dto;

import java.util.Set;

public record UpdateUserRolesRequest(
		Set<String> roles
) {}
