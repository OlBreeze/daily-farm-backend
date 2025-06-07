package org.dailyfarm.orders.entities;

import java.util.UUID;

import org.dailyfarm.products.entities.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Table(name = "order_products")
public class OrderProducts {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	private UUID id;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "id_order")
	private Order order;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "id_product")
	private Product product;
	
	private Double price;
	private Double quantity;
	
	private String comment;
}
