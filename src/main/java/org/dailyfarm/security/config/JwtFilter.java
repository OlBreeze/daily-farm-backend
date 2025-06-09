package org.dailyfarm.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dailyfarm.security.dto.RefreshTokenResponse;
import org.dailyfarm.security.service.auth.JwtService;
import org.dailyfarm.security.service.auth.RefreshTokenService;
import org.dailyfarm.service.constants.SecurityConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
	
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;
	private final RefreshTokenService refreshTokenService;
	 
	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //System.out.println("=== JwtFilter: DEBUG enabled = " + log.isDebugEnabled());

        // Пропускаем публичные эндпоинты
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractToken(request);
       // System.out.println("accessToken " +accessToken);
        // Если токена нет - пускаем дальше
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Пытаемся валидировать access token
            Claims tokenClaims = jwtService.validateToken(accessToken);
            authenticateUser(tokenClaims);
            
        } catch (ExpiredJwtException e) {
            log.debug("Access token expired, attempting refresh");
            
            // Пытаемся обновить токен
            if (handleTokenRefresh(request, response)) {
				// Токен успешно обновлен, повторяем аутентификацию
				try {
					String newAccessToken = extractToken(request);
					if (newAccessToken != null) {
						Claims newClaims = jwtService.validateToken(newAccessToken);
						authenticateUser(newClaims);
					}
				} catch (Exception refreshException) {
					log.warn("Failed to authenticate with refreshed token", refreshException);
					clearSecurityContext();
				}
			} else {
				log.debug("Token refresh failed");
				clearSecurityContext();
			}

		} catch (JwtException e) {
			log.warn("Invalid JWT token: {}", e.getMessage());
			clearSecurityContext();

		} catch (UsernameNotFoundException e) {
			log.warn("User not found: {}", e.getMessage());
			clearSecurityContext();

		} catch (Exception e) {
			log.error("Unexpected error during JWT processing", e);
			clearSecurityContext();
		}

		filterChain.doFilter(request, response);
	}

	private void authenticateUser(Claims tokenClaims) {
        final String username = (String) tokenClaims.get(SecurityConstants.USER);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Загружаем пользователя из БД (с ролями)
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // Создаем токен аутентификации
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            
            // Добавляем детали запроса
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
            ));
            
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }
	
	private String extractToken(HttpServletRequest request) {
		// 1. Пытаемся взять токен из Cookie
		if (request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if ("accessToken".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}

		// 2. Если нет в Cookie — ищем в Authorization header
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && authHeader.startsWith(SecurityConstants.BEARER_PREFIX)) {
			return authHeader.substring(SecurityConstants.BEARER_PREFIX.length());
		}

		return null;
	}

	private void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("path" + path);
        // Список публичных эндпоинтов, которые не требуют аутентификации, потом придумать
        return path.startsWith("/api/auth/**") || path.startsWith("/login")||path.startsWith("/logout")||
               path.startsWith("/account/register") ||
               path.equals("/test1");
    }
    private boolean handleTokenRefresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Извлекаем refresh token
            String refreshToken = extractRefreshToken(request);
            if (refreshToken == null) {
                return false;
            }

            // Проверяем и обновляем токены		
            RefreshTokenResponse tokenResponse = refreshTokenService.refreshTokens(refreshToken);
            
            if (tokenResponse != null) {
                // Устанавливаем новые токены в cookies
                setTokenCookies(response, tokenResponse);
                return true;
            }
            
        } catch (Exception e) {
            log.warn("Token refresh failed", e);
        }
        
        return false;
    }
    
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    private void setTokenCookies(HttpServletResponse response, RefreshTokenResponse tokenResponse) {
        // Access Token Cookie
        Cookie accessTokenCookie = new Cookie("accessToken", tokenResponse.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true); // только для HTTPS
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(tokenResponse.getAccessTokenExpiresIn()));
        response.addCookie(accessTokenCookie);

        // Refresh Token Cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(tokenResponse.getRefreshTokenExpiresIn()));
        response.addCookie(refreshTokenCookie);
    }

//	// вернет true для определенных ендпоитов
//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//		// TODO Auto-generated method stub
//		return super.shouldNotFilter(request);
//	}

}
