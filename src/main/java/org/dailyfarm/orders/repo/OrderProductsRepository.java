package org.dailyfarm.orders.repo;

import java.util.List;
import java.util.UUID;

import org.dailyfarm.orders.entities.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, UUID>{

	List<OrderProducts> findByOrderId(UUID id);

}
