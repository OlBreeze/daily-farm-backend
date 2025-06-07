package org.dailyfarm.products.controllers;

import static org.dailyfarm.service.api.ApiConstants.*;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.products.dto.CategoryDto;
import org.dailyfarm.products.service.ICategoriesService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
//@RequestMapping("/products")
@RequiredArgsConstructor
public class CategoriesController {
	
	private final ICategoriesService categoryService;
	
	@GetMapping(GET_ALL_CATEGORIES)
	List<CategoryDto> getAllCategories(){
		return categoryService.getAllCategories();
	}
	
	@GetMapping(ADD_CATEGORY)
	public CategoryDto getCategory(@PathVariable UUID id) {
	    return categoryService.getCategory(id);
	}
}
