package org.dailyfarm.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import org.dailyfarm.security.entity.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

	Role findByName(String role);

}
