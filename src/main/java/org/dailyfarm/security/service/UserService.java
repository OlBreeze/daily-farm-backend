package org.dailyfarm.security.service;

import java.util.HashSet;
import java.util.List;

import org.dailyfarm.security.dto.UserDto;
import org.dailyfarm.security.entity.Role;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.security.repository.RoleRepository;
import org.dailyfarm.security.repository.UserRepository;
import org.dailyfarm.security.service.mappers.UserMapper;
import org.dailyfarm.service.constants.SecurityConstants;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	
	public void createUser(UserDto dto) {
		User newUser = userMapper.dtoToEntity(dto);
		newUser.setPassword(hashPassword(newUser.getPassword()));
		HashSet<Role> setRoles = new HashSet<>();
		
		for (String nameRole : SecurityConstants.DEFAULT_ROLES) {
			Role role = roleRepository.findByName(nameRole);
			setRoles.add(role);
		}
		newUser.setRoles(setRoles);
		
		userRepository.save(newUser);
	}
	
	private String hashPassword(String rawPassword) {
		String hashedPassword = passwordEncoder.encode(rawPassword);
		return hashedPassword;
	}
	
	public List<UserDto> getAllUsers() {
	    return userRepository.findAll().stream()
	        .map(userMapper::entityToDto)
	        .toList();
	}
	
}
