package org.dailyfarm.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
	 private String accessToken;
     private String refreshToken;
     private Long accessTokenExpiresIn;
     private Long refreshTokenExpiresIn;
}
