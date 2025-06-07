package org.dailyfarm.security.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.dailyfarm.security.config.JwtConfig;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.service.constants.SecurityConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
	
	private final JwtConfig jwtConfig;
	
	// генерим токен
	public String generateToken(User userToAuth) {
		return Jwts.builder()
				.id(UUID.randomUUID().toString()) // просто иД токена
				.claim(SecurityConstants.USER, userToAuth.getUsername())//claim - ключ, это будет в Payload
				//.claim("roles", getRolesFormatted(userToAuth.getRoles())) //возьмем из базы
				.issuedAt(new Date()) // именно так
				.expiration(new Date(System.currentTimeMillis() + Duration.ofMinutes(jwtConfig.expirationMinutes()).toMillis()))
				.signWith(getSecret(jwtConfig.secret())) 	// обязательно
				.compact();				// обязательно
	}

//	разбираем полученный токен
	public Claims validateToken(String token) {
		return Jwts.parser()
				.verifyWith(getSecret(jwtConfig.secret()))
				.build()
				.parse(token)
				.accept(Jws.CLAIMS)
				.getPayload();
	}
	
	//возьмем из базы
//	private Set<String> getRolesFormatted(Set<Role> roles) {
//		return roles.stream().map(Role::getName).collect(Collectors.toSet());
//	}
	
	private SecretKey getSecret(String secret) {
		return Keys
				.hmacShaKeyFor(secret.getBytes());
	}
	
	public boolean isValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        Claims claims = validateToken(token);
        return claims.getSubject(); // или другой claim, например "username"
    }

	public String getCurrentUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("User not authenticated");
		}

		return authentication.getName(); // это username из JWT токена
	}

	public String getRolesUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new RuntimeException("User not authenticated");
		}

		// Получаем роли из Authentication
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		String roles = authorities.stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(", "));

		return roles;
	}

    public Long getAccessTokenExpirationTime() {
        return jwtConfig.expirationMinutes() * 60L; // в секундах
    }

    public Long getRefreshTokenExpirationTime() {
        return jwtConfig.refreshExpirationDays() * 24 * 60 * 60L; // в секундах
    }

}
