package org.dailyfarm.service.api.auth;

@SuppressWarnings("serial")
public class LoginException extends RuntimeException{

	public LoginException(String message) {
		super(message);
	}
}
