package org.dailyfarm.security.service.mappers;

import org.dailyfarm.products.entities.Product;
import org.dailyfarm.security.dto.UserDto;
import org.dailyfarm.security.entity.Role;
import org.dailyfarm.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "roles", qualifiedByName = "uuidsToRoles")
    User dtoToEntity(UserDto dto);

    @Mapping(target = "products", qualifiedByName = "productsToUuids")
    @Mapping(target = "roles", qualifiedByName = "rolesToUuids")
    UserDto entityToDto(User user);

    // Методы для маппинга ролей
    @Named("rolesToUuids")
    default Set<UUID> mapRolesToUuids(Set<Role> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }

    @Named("uuidsToRoles")
    default Set<Role> mapUuidsToRoles(Set<UUID> roleIds) {
        if (roleIds == null) {
            return Set.of();
        }
        return roleIds.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }

    // Методы для маппинга продуктов
    @Named("productsToUuids")
    default Set<UUID> mapProductsToUuids(Set<Product> products) {
        if (products == null) {
            return Set.of();
        }
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    @Named("uuidsToProducts")
    default Set<Product> mapUuidsToProducts(Set<UUID> productIds) {
        if (productIds == null) {
            return Set.of();
        }
        return productIds.stream()
                .map(id -> {
                    Product product = new Product();
                    product.setId(id);
                    return product;
                })
                .collect(Collectors.toSet());
    }
}
