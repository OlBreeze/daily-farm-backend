package org.dailyfarm.security.config;

import org.dailyfarm.service.constants.ValidationConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfig(
		@NotBlank
		String secret,
		
		@NotNull
		@Min(value = ValidationConstants.MIN_JWT_EXPIRATION_MINUTES)
		//@Value(value = "expiration.minutes")
		Integer expirationMinutes,
		
		@NotNull
		@Min(value = ValidationConstants.MIN_JWT_EXPIRATION_DAYS)
		//@Value(value = "refresh.expiration.days")
		Integer refreshExpirationDays // (for refresh token)
) {
	
//	@PostConstruct
//	void test() {
//		System.out.println(secret);
//		System.out.println(expirationMinutes);
//	}
	
}
