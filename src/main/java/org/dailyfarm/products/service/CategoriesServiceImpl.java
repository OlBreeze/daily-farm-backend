package org.dailyfarm.products.service;

import static org.dailyfarm.service.api.ExceptionMessageConstants.*;
import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.CategoryDto;
import org.dailyfarm.products.entities.Category;
import org.dailyfarm.products.repo.CategoriesRepository;
import org.dailyfarm.products.service.mappers.CategoryMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements ICategoriesService{
	private final CategoryMapper CategoryMapper;
	private final CategoriesRepository CategorysRepo;
	
	@Override
	public List<CategoryDto> getAllCategories() {
		return CategorysRepo.findAll().stream()
		        .map(CategoryMapper::toDto)
		        .toList();
	}
	@Override
	public CategoryDto getCategory(UUID id) {
		if (id == null)
			throw new IllegalArgumentException(WRONG_ARGUMENT);

		Category pr = CategorysRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_EXISTS));

		return CategoryMapper.toDto(pr);
	}
	
	@Transactional
	@Override
	public void addCategory(CategoryDto dto) {
		Category entity = CategoryMapper.toEntity(dto);
		CategorysRepo.save(entity);
		
	}
	
	@Transactional
	@Override
	public CategoryDto removeCategory(UUID id) {
		Category pr = CategorysRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_EXISTS));
		CategorysRepo.delete(pr);
		return CategoryMapper.toDto(pr);
	}
	
	@Transactional
	@Override
	public CategoryDto updateCategory(UUID id, CategoryDto dto) {
		Category existingCategory = CategorysRepo.findById(id)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, CATEGORY_NOT_EXISTS));

		    if (dto.getName()!=null)
		    	existingCategory.setName(dto.getName());
			return dto;
	}
}
