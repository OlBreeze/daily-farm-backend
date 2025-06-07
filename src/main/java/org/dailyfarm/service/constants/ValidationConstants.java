package org.dailyfarm.service.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationConstants {

	public static final int MIN_PAGE_SIZE = 1;
	public static final int MIN_PAGE_NUMBER = 1;
	
	public static final int MIN_JWT_EXPIRATION_MINUTES = 5;
	public static final int MIN_JWT_EXPIRATION_DAYS = 1;
}
