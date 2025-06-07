package org.dailyfarm.service.api.auth;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ResponseStatus(code=HttpStatus.BAD_REQUEST)
	@ExceptionHandler(LoginException.class)
	public ApiErrorResponse handleLoginException(LoginException e) {
		return new ApiErrorResponse(e.getMessage());
	}
}
