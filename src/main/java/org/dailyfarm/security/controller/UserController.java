package org.dailyfarm.security.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dailyfarm.security.dto.UserDto;
import org.dailyfarm.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import static org.dailyfarm.service.api.ApiConstants.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
//@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping(REGISTER_USER)
	public ResponseEntity<Map<String, Object>> createUser(@RequestBody @Valid UserDto dto) {
	    userService.createUser(dto);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("success", true);
	    response.put("message", "Registration successful");
	    
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping(GET_ALL_USERS)
	public List<UserDto> getAllUsers() {
		return userService.getAllUsers();
	}
	
//	@GetMapping
//	public String test() {
//		return "hello";
//	}
}
