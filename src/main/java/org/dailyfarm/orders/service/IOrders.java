package org.dailyfarm.orders.service;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.orders.dto.OrderDto;
import org.dailyfarm.orders.dto.OrderProductsDto;
import org.dailyfarm.orders.dto.OrderRequest;

public interface IOrders {
	
	List<OrderDto> getAllOrders();
	OrderDto removeOrder(UUID id);
	OrderDto updateOrder(UUID id, OrderDto dto);
	
    void addProductToOrder(OrderProductsDto orderProductsDto);
    void removeProductFromOrder(UUID orderId, UUID productId);
    OrderDto getOrderById(UUID id);
    List<OrderDto> getOrdersByUser(UUID userId);
    void finalizeOrder(UUID orderId);// Завершить заказ (оформить)
	//Order createOrder(OrderDto orderDto, List<OrderProductsDto> productDtos);
    OrderDto createOrder(OrderRequest orderRequest);
}
