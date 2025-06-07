package org.dailyfarm.products.service.mappers;

import org.dailyfarm.products.dto.ProductDto;
import org.dailyfarm.products.entities.Category;
import org.dailyfarm.products.entities.Product;
import org.dailyfarm.products.entities.Unit;
import org.dailyfarm.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	@Mapping(source = "category.id", target = "categoryId")
	@Mapping(source = "category.name", target = "categoryName")
	@Mapping(source = "unit.id", target = "unitId")
	@Mapping(source = "unit.name", target = "unitName")
	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "user.username", target = "username")
	ProductDto toDto(Product product);
	
	@Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
	@Mapping(source = "unitId", target = "unit", qualifiedByName = "mapUnit")
	@Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
	Product toEntity(ProductDto dto);

	@Named("mapCategory")
    default Category mapCategory(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    @Named("mapUnit")
    default Unit mapUnit(UUID unitId) {
        if (unitId == null) {
            return null;
        }
        Unit unit = new Unit();
        unit.setId(unitId);
        return unit;
    }
    
    // проверить по безопасности и ролям
    @Named("mapUser")
    default User mapUser(UUID userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }
}

/*
Uses:
private ProductMapper productMapper;

ProductDto dto = productMapper.toDto(product);
Product entity = productMapper.toEntity(dto);
*/
