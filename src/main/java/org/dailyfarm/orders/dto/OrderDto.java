package org.dailyfarm.orders.dto;

import java.time.LocalDateTime;
import java.util.List;
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
	public class OrderDto {
		private UUID id;
		private UUID userId;
		private String username;
		
		private Double totalSum;
		private Double totalAmount;
		
		private LocalDateTime dateOrder;
		private LocalDateTime datePayment;
		
		private String statusDelivery;
		private String comment;
		private List<OrderProductsDto> products;
	}
