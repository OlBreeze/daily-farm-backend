package org.dailyfarm.security.utils.validation.annotation;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.dailyfarm.security.utils.validation.validator.OnlyExistingRolesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RUNTIME)
@Target(PARAMETER)
@Constraint(validatedBy = OnlyExistingRolesValidator.class)
public @interface OnlyExistingRoles{

	String message() default "some roles specified are not existing";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
