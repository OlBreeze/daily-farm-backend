package org.dailyfarm.orders.service;

import lombok.RequiredArgsConstructor;
import org.dailyfarm.orders.dto.OrderDto;
import org.dailyfarm.orders.dto.OrderProductsDto;
import org.dailyfarm.orders.dto.OrderRequest;
import org.dailyfarm.orders.entities.Order;
import org.dailyfarm.orders.entities.OrderProducts;
import org.dailyfarm.orders.repo.OrderProductsRepository;
import org.dailyfarm.orders.repo.OrdersRepository;
import org.dailyfarm.orders.service.mappers.OrderMapper;
import org.dailyfarm.orders.service.mappers.OrderProductsMapper;
import org.dailyfarm.products.entities.Product;
import org.dailyfarm.products.repo.ProductsRepository;
import org.dailyfarm.security.entity.User;
import org.dailyfarm.security.repository.UserRepository;
import org.dailyfarm.security.service.auth.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.dailyfarm.service.api.ExceptionMessageConstants.ORDER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements IOrders {
	private final OrderMapper orderMapper;
	private final OrderProductsMapper orderProductsMapper;
	private final OrdersRepository orderRepository;
	private final OrderProductsRepository orderProductsRepository;
	private final ProductsRepository productRepository;
	private final UserRepository userRepository;
	private final JwtService jwtService;

	@Transactional(readOnly = true)
	@Override
	public List<OrderDto> getAllOrders() {
		return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
	}

	@Override
	@Transactional
	public OrderDto createOrder(OrderRequest orderRequest) {
		
		OrderDto orderDto = orderRequest.getOrder();
		List<OrderProductsDto> productDto = orderRequest.getProducts();
		// Получаем username из Security Context (из JWT токена)

		String username = jwtService.getCurrentUsername();
		System.out.println("username: "  + username);
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found: " + username));

		Order order = new Order();
		order.setUser(user);
		order.setStatusDelivery("NEW");
		order.setComment(orderDto.getComment());
		order.setDateOrder(LocalDateTime.now());
		order.setTotalAmount(0.0);
		order.setTotalSum(0.0);

		order = orderRepository.save(order); // сохраняем сначала заказ — нужен id

		double totalSum = 0.0;
		double totalAmount = 0.0;

		for (OrderProductsDto dto : productDto) {
			Product product = productRepository.findById(dto.getProductId())
					.orElseThrow(() -> new RuntimeException("Product not found"));

			OrderProducts orderProduct = new OrderProducts();
			orderProduct.setOrder(order);
			orderProduct.setProduct(product);
			orderProduct.setPrice(dto.getPrice());
			orderProduct.setQuantity(dto.getQuantity());
			orderProduct.setComment(dto.getComment());

			orderProductsRepository.save(orderProduct);

			totalSum += dto.getPrice() * dto.getQuantity();
			totalAmount += dto.getQuantity();
		}

		order.setTotalSum(totalSum);
		order.setTotalAmount(totalAmount);
		return orderMapper.toDto(order); // обновляем сумму заказа
	}

	@Override
	@Transactional
	public void addProductToOrder(OrderProductsDto orderProductsDto) {
		Order order = orderRepository.findById(orderProductsDto.getOrderId())
				.orElseThrow(() -> new RuntimeException("Order not found"));
		Product product = productRepository.findById(orderProductsDto.getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found"));

		OrderProducts item = new OrderProducts();
		item.setOrder(order);
		item.setProduct(product);
		item.setQuantity(orderProductsDto.getQuantity());
		item.setPrice(orderProductsDto.getPrice());
		item.setComment(orderProductsDto.getComment());
		orderProductsRepository.save(item);

		recalculateOrderTotal(order);
	}

	@Override
	@Transactional
	public void removeProductFromOrder(UUID orderId, UUID productId) {
		List<OrderProducts> items = orderProductsRepository.findByOrderId(orderId);
		for (OrderProducts item : items) {
			if (item.getProduct().getId().equals(productId)) {
				orderProductsRepository.delete(item);
				break;
			}
		}
		Order order = orderRepository.findById(orderId).orElseThrow();
		recalculateOrderTotal(order);
	}

//    @Override
//    public Order getOrderById(UUID orderId) {
//        return orderRepository.findById(orderId)
//           
//    .orElseThrow(() -> new RuntimeException("Order not found"));
//    }
	
	@Transactional(readOnly = true)
	@Override
	public OrderDto getOrderById(UUID orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		OrderDto ordDto = orderMapper.toDto(order); // предполагается, что это маппинг самого заказа

		return ordDto;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<OrderDto> getOrdersByUser(UUID userId) {
		return orderRepository.findByUserId(userId);
	}

	private void recalculateOrderTotal(Order order) {
		List<OrderProducts> items = orderProductsRepository.findByOrderId(order.getId());

		double totalAmount = items.stream().mapToDouble(OrderProducts::getQuantity).sum();

		double totalSum = items.stream().mapToDouble(i -> i.getQuantity() * i.getPrice()).sum();

		order.setTotalAmount(totalAmount);
		order.setTotalSum(totalSum);
		orderRepository.save(order);
	}

	@Override
	@Transactional
	public void finalizeOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		order.setStatusDelivery("CONFIRMED");
		order.setDatePayment(LocalDateTime.now());
		orderRepository.save(order);
	}

	@Transactional
	@Override
	public OrderDto removeOrder(UUID id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_EXISTS));
		orderRepository.delete(order);
		return orderMapper.toDto(order);
	}

	@Transactional
	@Override
//	public OrderDto updateOrder(UUID id, OrderDto dto) {
//		Order existOrder = orderRepository.findById(id)
//				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_EXISTS));
//		
//		if (dto.getComment()!=null)
//			existOrder.setComment(dto.getComment());
//		
//		if (dto.getStatusDelivery()!=null)
//			existOrder.setStatusDelivery(dto.getStatusDelivery());
//		
//		if (dto.getDatePayment()!=null)
//			existOrder.setDatePayment(null);
//		
//		orderRepository.save(existOrder);
//		
//		return orderMapper.toDto(existOrder);
//	}
	public OrderDto updateOrder(UUID id, OrderDto dto) {
		Order existOrder = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ORDER_NOT_EXISTS));

		if (dto.getComment() != null)
			existOrder.setComment(dto.getComment());

		if (dto.getStatusDelivery() != null)
			existOrder.setStatusDelivery(dto.getStatusDelivery());

		if (dto.getDatePayment() != null)
			existOrder.setDatePayment(dto.getDatePayment());

		if (OrderProductsComparator.isChanged(dto.getProducts(), existOrder.getOrderProducts())) {
			
			existOrder.getOrderProducts().clear();

			// Добавляем новые
			List<OrderProducts> newProducts = dto.getProducts().stream()
					.map(orderProductsMapper::toEntity)
					.collect(Collectors.toList());

			existOrder.getOrderProducts().addAll(newProducts);
			
			double totalSum = 0.0;
		    double totalAmount = 0.0;

		    for (OrderProducts product : newProducts) {
		        if (product.getPrice() != null && product.getQuantity() != null) {
		            totalSum    += product.getPrice() * product.getQuantity();
		            totalAmount += product.getQuantity();
		        }
		    }
		    
		    existOrder.setTotalSum(totalSum);
		    existOrder.setTotalAmount(totalAmount);
		}
		orderRepository.save(existOrder);
		return orderMapper.toDto(existOrder);
	}

}
