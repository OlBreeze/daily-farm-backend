package org.dailyfarm.service.api.auth;

public class RefreshTokenException extends RuntimeException {
	private static final long serialVersionUID = -3905440751663409315L;

	public RefreshTokenException(String message) {
        super(message);
    }
}
