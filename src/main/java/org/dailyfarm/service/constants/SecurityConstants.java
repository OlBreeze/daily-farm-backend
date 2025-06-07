package org.dailyfarm.service.constants;

import java.util.HashSet;
import java.util.Set;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityConstants {
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String USER = "user";
	public static final Set<String> DEFAULT_ROLES;

	static {
	    DEFAULT_ROLES = new HashSet<>();
	    DEFAULT_ROLES.add("USER");
	   // DEFAULT_ROLES.add("name_roles");
	}
}

