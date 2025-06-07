package org.dailyfarm.security.utils.validation.validator;

import java.util.Collection;
import java.util.UUID;

import org.dailyfarm.security.repository.RoleRepository;
import org.dailyfarm.security.utils.validation.annotation.OnlyExistingRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class OnlyExistingRolesValidator implements ConstraintValidator<OnlyExistingRoles, Collection<UUID>>{

	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public boolean isValid(Collection<UUID> value, ConstraintValidatorContext context) {
		boolean isValid = true;
		if (value != null && !value.isEmpty()) {
			isValid &= value.stream()
				.allMatch(s -> roleRepository.existsById(s));
		}
		return isValid;
	}

}