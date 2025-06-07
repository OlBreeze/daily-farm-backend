package org.dailyfarm.security.dto;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.dailyfarm.security.utils.validation.annotation.OnlyExistingRoles;

public record UserDto(
		UUID id,
		String username,
		String password,
		String email,
		Boolean client,
		Boolean company,
		LocalDate dateCreate,
		LocalDate lastDateUpdate,
		String address,
		String phone,
		boolean revoked,
		
		@OnlyExistingRoles
		Set<UUID> roles,
		
		Set<UUID> products){}





