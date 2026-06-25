package org.yearup.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	private OrderRepository orderRepository;
	private OrderLineItemRepository orderLineItemRepository;
	private ProfileRepository profileRepository;
	private ProductService productService;

	@Autowired
	public OrderService(OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository,
	                    ProfileRepository profileRepository, ProductService productService, ShoppingCartRepository shoppingCartRepository) {
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
		this.profileRepository = profileRepository;
		this.productService = productService;
		this.shoppingCartRepository = shoppingCartRepository;
	}

	@Transactional
	public Order create(int userId) {
		var profile = profileRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Profile not found with user id: " + userId));

		var cartItems = shoppingCartRepository.findByUserId(userId);   // gets all items in shopping cart

		if (cartItems == null || cartItems.isEmpty())
			return null;

		var newOrder = new Order();     // to create an order record

//		newOrder.setOrderId();
		newOrder.setUserId(userId);
		newOrder.setDate(LocalDate.now());
		newOrder.setAddress(profile.getAddress());
		newOrder.setCity(profile.getCity());
		newOrder.setState(profile.getState());
		newOrder.setZip(profile.getZip());
		newOrder.setShippingAmount(0);

		// creating order object to generate orderId (not saved)
		Order savedOrder = orderRepository.save(newOrder);

		// creating one orderLineItem for each cart item and saving it
		for (var cartItem : cartItems) {
			Product product = productService.getById(cartItem.getProductId());  // use productId to find full Product object
			OrderLineItem orderLineItem = new OrderLineItem();

			orderLineItem.setOrderId(savedOrder.getOrderId());
			orderLineItem.setProductId(product.getProductId());
			orderLineItem.setSalesPrice(product.getPrice());
			orderLineItem.setQuantity(cartItem.getQuantity());
			orderLineItem.setDiscount(orderLineItem.getDiscount());


			orderLineItemRepository.save(orderLineItem);    // saving each orderLineItem into the db
		}

		shoppingCartRepository.deleteByUserId(userId);      // clearing cart

		return savedOrder;
	}


}
