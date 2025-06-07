package org.dailyfarm.security.service.auth;

import org.dailyfarm.security.dto.LoginDto;
import org.dailyfarm.security.dto.LoginResponse;
import org.dailyfarm.security.dto.RefreshTokenRequest;
import org.dailyfarm.security.entity.RefreshToken;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.security.repository.UserRepository;
import org.dailyfarm.service.api.auth.LoginException;
import org.dailyfarm.service.api.auth.RefreshTokenException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository userRepository;

	private final JwtService jwtService;
	private final PasswordEncoder PasswordEncoder;
	
	private final RefreshTokenService refreshTokenService;
	
	@Transactional //  (readOnly = true)  // теперь мы записываем refresh token
	public LoginResponse login(LoginDto dto) {
		User userToAuth = userRepository.findByUsername(dto.username())
				.orElseThrow(() -> new LoginException("username or password not correct"));
				//.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
																						
		if (!PasswordEncoder.matches(dto.password(), userToAuth.getPassword())) {
			throw new LoginException("username or password not correct");
		}
		String accessToken = jwtService.generateToken(userToAuth);
		String refreshToken = refreshTokenService.generateRefreshToken(userToAuth.getId()).getToken();
        
		return new LoginResponse(accessToken, refreshToken); // accessToken - подписываем теперь все наши запросы от клиента
	}
	
	@Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return new LoginResponse(accessToken, request.refreshToken());
                })
                .orElseThrow(() -> new RefreshTokenException("Refresh token is not in database!"));
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenService.deleteByToken(refreshToken);
    }

}
