package org.dailyfarm.orders.repo;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.orders.dto.OrderDto;
import org.dailyfarm.orders.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, UUID>{
//	@EntityGraph(attributePaths = "orderProducts")
//	Optional<Order> findById(UUID id);
	
	List<OrderDto> findByUserId(UUID userId);

}
