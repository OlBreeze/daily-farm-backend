package org.dailyfarm.orders.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class OrderProductsDto {
	private UUID id;
	private UUID orderId;
	
	private UUID productId;
	private String productName;
	
	private Double price;
	private Double quantity;
	
	private String comment;
	
}
