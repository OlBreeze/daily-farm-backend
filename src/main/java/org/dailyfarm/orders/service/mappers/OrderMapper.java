package org.dailyfarm.orders.service.mappers;

import java.util.UUID;

import org.dailyfarm.orders.dto.OrderDto;
import org.dailyfarm.orders.entities.Order;
import org.dailyfarm.security.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {OrderProductsMapper.class})
public interface OrderMapper {
	
	@Mapping(source = "user.id", target = "userId")
	@Mapping(source = "user.username", target = "username")
	@Mapping(source = "orderProducts", target = "products") 
	OrderDto toDto(Order order);
	
	@Mapping(source = "userId", target = "user", qualifiedByName = "mapUser")
	@Mapping(source = "products", target = "orderProducts")
	Order toEntity(OrderDto orderDto);
	
	
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
