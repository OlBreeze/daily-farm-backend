package org.dailyfarm.products.repo;

import java.util.UUID;

import org.dailyfarm.products.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Category, UUID>{

}
