package org.dailyfarm.products.service;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.CategoryDto;

public interface ICategoriesService {
	List<CategoryDto> getAllCategories();
	
	CategoryDto getCategory(UUID id);
	void addCategory(CategoryDto unit);
	CategoryDto removeCategory(UUID id);
	CategoryDto updateCategory(UUID id, CategoryDto dto);
}
