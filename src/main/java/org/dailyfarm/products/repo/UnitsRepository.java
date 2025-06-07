package org.dailyfarm.products.repo;

import java.util.UUID;

import org.dailyfarm.products.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitsRepository extends JpaRepository<Unit, UUID>{

}
