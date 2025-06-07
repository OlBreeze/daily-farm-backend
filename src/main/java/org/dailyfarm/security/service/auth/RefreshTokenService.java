package org.dailyfarm.security.service.auth;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.dailyfarm.security.config.JwtConfig;
import org.dailyfarm.security.dto.RefreshTokenResponse;
import org.dailyfarm.security.entity.RefreshToken;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.security.repository.RefreshTokenRepository;
import org.dailyfarm.security.repository.UserRepository;
import org.dailyfarm.service.api.auth.RefreshTokenException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenService {

    private final JwtConfig jwtConfig;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService; // Добавляем JwtService

    public RefreshToken generateRefreshToken(UUID userId) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findById(userId).orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId)))
                .expiryDate(Instant.now().plusSeconds(jwtConfig.refreshExpirationDays() * 24 * 60 * 60))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    // Новый метод для обновления токенов (для JWT фильтра)
    public RefreshTokenResponse refreshTokens(String refreshTokenStr) {
        try {
            RefreshToken refreshToken = findByToken(refreshTokenStr)
                    .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));

            // Проверяем истечение
            verifyExpiration(refreshToken);

            User user = refreshToken.getUser();
            
            // Генерируем новый access token
            String newAccessToken = jwtService.generateToken(user);
            
            // Опционально: генерируем новый refresh token (для ротации)
            String newRefreshToken = generateNewRefreshToken(user);
            
            // Удаляем старый refresh token если генерируем новый
            if (newRefreshToken != null) {
                refreshTokenRepository.delete(refreshToken);
            }

            log.info("Tokens refreshed successfully for user: {}", user.getUsername());

            return RefreshTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken != null ? newRefreshToken : refreshTokenStr)
                    .accessTokenExpiresIn(jwtConfig.expirationMinutes() * 60L) // в секундах
                    .refreshTokenExpiresIn(jwtConfig.refreshExpirationDays() * 24 * 60 * 60L) // в секундах
                    .build();

        } catch (RefreshTokenException e) {
            log.warn("Refresh token error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during token refresh", e);
            throw new RefreshTokenException("Token refresh failed");
        }
    }

    // Опционально: генерируем новый refresh token для ротации
    private String generateNewRefreshToken(User user) {
        // Если хотите ротацию refresh token - раскомментируйте
        // RefreshToken newRefreshToken = generateRefreshToken(user.getId());
        // return newRefreshToken.getToken();
        
        // Если не хотите ротацию - возвращаем null
        return null;
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + userId));
        refreshTokenRepository.deleteByUser(user);
    }

    // Дополнительные методы для удобства
    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User not found with username: " + username));
        refreshTokenRepository.deleteByUser(user);
    }

    public void revokeAllUserTokens(String username) {
        deleteByUsername(username);
    }

}