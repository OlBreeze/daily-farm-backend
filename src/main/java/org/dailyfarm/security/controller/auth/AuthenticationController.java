package org.dailyfarm.security.controller.auth;

import org.dailyfarm.security.dto.LoginDto;

import org.dailyfarm.security.dto.LoginResponse;
import org.dailyfarm.security.dto.RefreshTokenRequest;
import org.dailyfarm.security.service.auth.AuthenticationService;
import org.dailyfarm.security.service.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import static org.dailyfarm.service.api.ApiConstants.*;

import java.util.Map;

@CrossOrigin(origins = { "http://127.0.0.1:3000", "http://localhost:3000" }, methods = { RequestMethod.GET,
		RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT })
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationService authenticationService;
	private final JwtService jwtService;

    @PostMapping(LOGIN)
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
    		LoginResponse loginResponse = authenticationService.login(loginDto);
        
        Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.accessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // true для HTTPS в продакшне
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(15 * 60); // 15 минут
        response.addCookie(accessTokenCookie);
        
        Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.refreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // true для HTTPS в продакшне
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
        response.addCookie(refreshTokenCookie);
        
        return ResponseEntity.ok(Map.of("message", "Login successful"));
        
//		// Можно также вернуть какой-то JSON, если надо (например, username)
////    return ResponseEntity.ok(Map.of("message", "Login successful"));
////    return ResponseEntity.ok(Map.of("message", dto)); // логин пароль
//	return ResponseEntity.ok(Map.of("message", loginResponse)); // токен, для теста
        
    }

    @PostMapping(REFRESH)
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookie(request, "refreshToken");
        
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Refresh token not found"));
        }
        
        try {
            RefreshTokenRequest refreshRequest = new RefreshTokenRequest(refreshToken);
            LoginResponse loginResponse = authenticationService.refreshToken(refreshRequest);
            
            Cookie accessTokenCookie = new Cookie("accessToken", loginResponse.accessToken());
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false);
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(15 * 60); // 15 минут
            response.addCookie(accessTokenCookie);
            
//            // Опционально: обновляем refresh token 
//            if (loginResponse.refreshToken() != null) {
//                Cookie refreshTokenCookie = new Cookie("refreshToken", loginResponse.refreshToken());
//                refreshTokenCookie.setHttpOnly(true);
//                refreshTokenCookie.setSecure(false);
//                refreshTokenCookie.setPath("/");
//                refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 дней
//                response.addCookie(refreshTokenCookie);
//            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Token refreshed successfully"
            ));
            
        } catch (Exception e) {
            clearAuthCookies(response);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid refresh token"));
        }
    }

    @PostMapping(LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractTokenFromCookie(request, "refreshToken");
        
        if (refreshToken != null) {
            try {
                RefreshTokenRequest logoutRequest = new RefreshTokenRequest(refreshToken);
                authenticationService.logout(logoutRequest.refreshToken());
            } catch (Exception e) {
                System.err.println("Error during logout: " + e.getMessage());
            }
        }
        
        clearAuthCookies(response);
        
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    
    @GetMapping(AUTH_CHECK)
    public ResponseEntity<?> checkAuth(HttpServletRequest request) {
        // Проверяем access token из cookie (не из заголовка!)
        String accessToken = extractTokenFromCookie(request, "accessToken");
        
        if (accessToken != null && jwtService.isValid(accessToken)) {
            return ResponseEntity.ok(Map.of("message", "Token valid"));
        }
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid or missing token"));
    }
    
    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    private void clearAuthCookies(HttpServletResponse response) {
        // Очищаем access token cookie
        Cookie clearAccessToken = new Cookie("accessToken", "");
        clearAccessToken.setHttpOnly(true);
        clearAccessToken.setPath("/");
        clearAccessToken.setMaxAge(0); 
        response.addCookie(clearAccessToken);
        
        // Очищаем refresh token cookie
        Cookie clearRefreshToken = new Cookie("refreshToken", "");
        clearRefreshToken.setHttpOnly(true);
        clearRefreshToken.setPath("/");
        clearRefreshToken.setMaxAge(0);
        response.addCookie(clearRefreshToken);
    }
}
	// ниже версия когда у нас был только один аксес токен
//	
//	
//	@PostMapping(LOGOUT)
//	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
//		// Очистка сессии
//		HttpSession session = request.getSession(false);
//		if (session != null) {
//			session.invalidate();
//		}
//
//		// Очистка cookie
//		Cookie cookie = new Cookie("token", "");
//		cookie.setMaxAge(0);
//		cookie.setPath("/");
//		cookie.setHttpOnly(true);
//		response.addCookie(cookie);
//		//cookie.setSecure(false);
//
//		return ResponseEntity.ok().build();
//	}

//	@GetMapping(AUTH_CHECK)
//	public ResponseEntity<?> checkAuth(
//			@CookieValue(value = "token", required = false) String tokenFromCookie,
//			@RequestHeader(value = "Authorization", required = false) String authHeader
//			) {
//		//System.out.println("tokenFromCookie  "+tokenFromCookie);
//		
//	    if (tokenFromCookie == null) {
//	    	 if (authHeader != null && authHeader.startsWith("Bearer ")) {
//	    		 tokenFromCookie = authHeader.substring(7); // обрезаем "Bearer "
//	 	    }
//	    }
//		//System.out.println("authHeader: "+ authHeader);
//		
//	    if (tokenFromCookie != null && jwtService.isValid(tokenFromCookie)) {
//	    	//String username = tokenService.getUsername(token);
//	        //return ResponseEntity.ok(Map.of("username", username));
//	        return ResponseEntity.ok().build();
//	    }
//	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//	}
	
	
