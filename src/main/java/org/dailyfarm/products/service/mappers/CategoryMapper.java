package org.dailyfarm.products.service.mappers;

import java.util.UUID;

import org.dailyfarm.products.dto.CategoryDto;
import org.dailyfarm.products.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	@Mapping(source="parentCategory.id", target="parentCategoryId")
	CategoryDto toDto(Category category);
	
	@Mapping(source = "parentCategoryId", target = "parentCategory")
	Category toEntity(CategoryDto dto);
	
    default Category map(UUID id) {
        if (id == null) {
            return null;
        }
        Category category = new Category();
        category.setId(id);
        return category;
    }

    default UUID map(Category category) {
        return category != null ? category.getId() : null;
    }
}

