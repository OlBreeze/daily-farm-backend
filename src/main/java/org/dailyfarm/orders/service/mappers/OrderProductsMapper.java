package org.dailyfarm.orders.service.mappers;

import java.util.UUID;

import org.dailyfarm.orders.dto.OrderProductsDto;
import org.dailyfarm.orders.entities.Order;
import org.dailyfarm.orders.entities.OrderProducts;
import org.dailyfarm.products.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderProductsMapper {
	@Mapping(source = "order.id", target = "orderId")
	@Mapping(source = "product.id", target = "productId")
	@Mapping(source = "product.name", target = "productName")
	OrderProductsDto toDto(OrderProducts orderPr);
	
	@Mapping(source = "orderId",   target = "order",   qualifiedByName = "mapOrder")
	@Mapping(source = "productId", target = "product", qualifiedByName = "mapProduct")
	OrderProducts toEntity(OrderProductsDto orderPrDto);
	
	 @Named("mapOrder")
	    default Order mapOrder(UUID orderId) {
	        if (orderId == null) {
	            return null;
	        }
	        Order order = new Order();
	        order.setId(orderId);
	        return order;
	    }
	 
    @Named("mapProduct")
    default Product mapProduct(UUID productId) {
        if (productId == null) {
            return null;
        }
        Product pr = new Product();
        pr.setId(productId);
        return pr;
    }
}


