package org.dailyfarm.orders.controllers;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.orders.dto.OrderDto;
import org.dailyfarm.orders.dto.OrderProductsDto;
import org.dailyfarm.orders.dto.OrderRequest;
import org.dailyfarm.orders.service.IOrders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static org.dailyfarm.service.api.ApiConstants.*;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, 
methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
@RestController
@RequiredArgsConstructor
public class OrdersController {
	
	private final IOrders orderService;
	
	@GetMapping(GET_ALL_ORDERS)
	public List<OrderDto> getAllOrders() {
		return orderService.getAllOrders();
	}
	
	@GetMapping(GET_ORDER_ID)
	public OrderDto getOrderById(@PathVariable UUID id) {
		return orderService.getOrderById(id);
	}
	
	@GetMapping(GET_ORDERS_USER)
	public List<OrderDto> getOrdersByUser(UUID userId) {
		return orderService.getOrdersByUser(userId);
	}
	
	@PutMapping(UPDATE_ORDER)
	public OrderDto updateOrder(@PathVariable UUID id, @RequestBody OrderDto dto) {
	    return orderService.updateOrder(id, dto);
	}
	
	@DeleteMapping(REMOVE_ORDER)
	public OrderDto removeOrder(@PathVariable UUID id) {
		return orderService.removeOrder(id);
	}
	
	@PostMapping(ADD_ORDER)
	public OrderDto createOrder(@RequestBody OrderRequest orderRequest ) {
		return orderService.createOrder(orderRequest);
	}
	
	@DeleteMapping(REMOVE_PRODUCT_FROM_ORDER)
	public void removeProductFromOrder(@PathVariable UUID orderId, @PathVariable UUID productId) {
		orderService.removeProductFromOrder(orderId, productId);
		
	}
	
	public void addProductToOrder(OrderProductsDto orderProductsDto) {
		orderService.addProductToOrder(orderProductsDto);
		
	}
	

	public void finalizeOrder(UUID orderId) {
		// TODO Auto-generated method stub
		
	}

}
