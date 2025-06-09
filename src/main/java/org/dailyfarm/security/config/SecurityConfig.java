package org.dailyfarm.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity(debug = false)
//@EnableConfigurationProperties(JwtConfig.class)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final CorsConfigurationSource corsConfigurationSource;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.httpBasic(basic -> basic.disable())
				//.cors(cors -> cors.disable()) // CORS -> Cross Origin Resource
				.cors(cors -> cors.configurationSource(corsConfigurationSource))// CORS -> Cross Origin Resource
																						// Sharing
				.csrf(csrf -> csrf.disable()) // CSRF -> Cross Site Request Forging
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JSESSIONID=UUID;
																									// мы никакого
																									// состояния не
																									// сохраняем, все на
																									// токенах
				.formLogin(form -> form.disable()) // форма логина по умолчанию
				.headers(headers -> headers.frameOptions(frame -> frame.disable())) //для H2
				.logout(logout -> logout.disable()) // логаут по умолчанию - я хочу свой

				.exceptionHandling(eh -> eh.authenticationEntryPoint(authenticationEntryPoint())
						.accessDeniedHandler(accessDeniedHandler()))

				// ставлю свой фильтр jwtFilter для моих токенов перед
				// UsernamePasswordAuthenticationFilter
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

				.authorizeHttpRequests(requests -> requests
					    // Public (не требует авторизации)
					    .requestMatchers("/h2-console/**", "/test/public" ).permitAll() // разрешить доступ к H2
						.requestMatchers(HttpMethod.POST, "/api/auth/**", "/users").permitAll() // регистрация
					    .requestMatchers("/api/auth/**", "/account/register").permitAll()
					    .requestMatchers(HttpMethod.GET, "/products", "/products/pages", "/products/id/{id}",
					            "/products/name/{name}", "/units", "/units/{id}", "/categories", "/categories/{id}")
					    .permitAll()
						.requestMatchers("/test/**").authenticated() // остальные /test/** требуют авторизации
					    // Admin-only - ИСПРАВЛЕНО
					    .requestMatchers(HttpMethod.GET, "/users", "/users/{id}").hasRole("ADMIN")
					    .requestMatchers(HttpMethod.PATCH, "/users/{id}/activate", "/users/{id}/revoke").hasRole("ADMIN")
					    .requestMatchers(HttpMethod.PUT, "/user/{userId}/role/{roleId}").hasRole("ADMIN")
					    .requestMatchers(HttpMethod.DELETE, "/user/{userId}/role/{roleId}").hasRole("ADMIN")

					    // Authenticated (owner or ADMIN)
					    .requestMatchers(HttpMethod.GET, "/roles/{id}").hasAnyRole("ADMIN", "USER", "MANAGER")
					    .requestMatchers(HttpMethod.GET, "/orders", "/orders/id/{id}", "/orders/user/{id}")
					    .hasAnyRole("ADMIN", "USER") // only owner enforced inside controller
					    .requestMatchers(HttpMethod.DELETE, "/orders/remove/{id}", "/orders/remove_product/{id}")
					    .hasAnyRole("ADMIN", "USER") // only owner enforced inside controller
					    .requestMatchers(HttpMethod.POST, "/orders/add", "/orders/add_product")
					    .hasAnyRole("ADMIN", "USER")

					    // Product management (ADMIN, MANAGER)
					    .requestMatchers(HttpMethod.POST, "/products/add").hasAnyRole("ADMIN", "MANAGER")
					    .requestMatchers(HttpMethod.PUT, "/products/update/{id}").hasAnyRole("ADMIN", "MANAGER")
					    .requestMatchers(HttpMethod.DELETE, "/products/remove/{id}").hasAnyRole("ADMIN", "MANAGER")

					    // Order update (USER, MANAGER, ADMIN)
					    .requestMatchers(HttpMethod.PUT, "/orders/update/{id}").hasAnyRole("ADMIN", "MANAGER", "USER")

					    // Everything else forbidden
					    .anyRequest().denyAll())
					.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // or use Argon2
	}

	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write("{\"error\": \"Forbidden: " + accessDeniedException.getMessage() + "\"}");
			}

		};
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {

			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write("{\"error\": \"Unauthorized: " + authException.getMessage() + "\"}");
			}

		};
	}
}
