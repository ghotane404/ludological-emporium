package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yearup.models.*;
import org.yearup.repository.OrderLineItemRepository;
import org.yearup.repository.OrderRepository;
import org.yearup.repository.ProfileRepository;
import org.yearup.repository.ShoppingCartRepository;

import java.time.LocalDate;

@Service
public class OrderService {
	private final ShoppingCartRepository shoppingCartRepository;
	private final OrderRepository orderRepository;
	private final OrderLineItemRepository orderLineItemRepository;
	private final ProfileRepository profileRepository;
	private final ProductService productService;

	@Autowired
	public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
	                    ProfileRepository profileRepository, ProductService productService,
	                    ShoppingCartRepository shoppingCartRepository) {
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
		this.profileRepository = profileRepository;
		this.productService = productService;
		this.shoppingCartRepository = shoppingCartRepository;
	}

	@Transactional
	public Order create(int userId) {
		var profile = profileRepository.findById(userId).orElse(null);
		if (profile == null) return null;

		var cartItems = shoppingCartRepository.findByUserId(userId);   // gets all items in shopping cart
		if (cartItems == null || cartItems.isEmpty()) return null;

		var order = new Order();     // save order first so db can generate an orderId
		order.setUserId(userId);
		order.setDate(LocalDate.now());
		order.setAddress(profile.getAddress());
		order.setCity(profile.getCity());
		order.setState(profile.getState());
		order.setZip(profile.getZip());
		order.setShippingAmount(0);

		Order savedOrder = orderRepository.save(order);  // creating order object to generate orderId (not saved)

		// creating one orderLineItem for each cart item and saving it
		for (var cartItem : cartItems) {
			Product product = productService.getById(cartItem.getProductId());  // use productId to find full Product object
			OrderLineItem orderLineItem = new OrderLineItem();

			orderLineItem.setOrderId(savedOrder.getOrderId());
			orderLineItem.setProductId(product.getProductId());
			orderLineItem.setSalesPrice(product.getPrice());
			orderLineItem.setQuantity(cartItem.getQuantity());
			orderLineItem.setDiscount(0);

			orderLineItemRepository.save(orderLineItem);    // saving each orderLineItem into the db
		}

		shoppingCartRepository.deleteByUserId(userId);      // clearing cart

		return savedOrder;
	}
}
