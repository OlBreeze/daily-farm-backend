package org.dailyfarm.security.dto;

public record LoginResponse(
	    String accessToken,
	    String refreshToken
	) {}
