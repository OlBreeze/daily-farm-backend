package org.dailyfarm.security.service.auth;

import org.dailyfarm.security.repository.UserRepository;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		org.dailyfarm.security.entity.User user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User with username '%s' not found".formatted(username)));

		String[] roles = user.getRoles().stream().map(r -> "ROLE_" + r.getName()).toArray(String[]::new);

		return new User(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(roles));
	}

}

//метод loadUserByUsername вызывается автоматически Spring Security при логине пользователя
//Каждая роль пользователя конвертируется в формат ROLE_NAME, как требует Spring Security
//Spring Security ожидает, что роли начинаются с ROLE_, иначе они не сработают в hasRole("ADMIN")
