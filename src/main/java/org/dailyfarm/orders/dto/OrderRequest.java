package org.dailyfarm.orders.dto;

import java.util.List;

import lombok.Data;
@Data
public class OrderRequest {
	private OrderDto order;
    private List<OrderProductsDto> products;

}
