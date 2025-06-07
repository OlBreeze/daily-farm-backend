package org.dailyfarm.orders.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.dailyfarm.security.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // генерим автоматически (UUID, IDENTITY чаще всего)
	private UUID id;
	
	@ManyToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "id_user")
	private User user;
	
	private Double totalSum;
	private Double totalAmount;
	
	private String statusDelivery;
	private String comment;
	private LocalDateTime dateOrder;
	private LocalDateTime datePayment;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderProducts> orderProducts;
	
}
