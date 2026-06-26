package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.net.URI;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/orders")
@CrossOrigin
public class OrdersController {
	private final OrderService orderService;
	private final UserService userService;

	public OrdersController(OrderService orderService,  UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@PostMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Order> createOrder(Principal principal) {
		int userId = getUserId(principal);
		var newOrder = orderService.create(userId);

		if (newOrder == null) return ResponseEntity.badRequest().build();

		URI location = URI.create("/orders/" + newOrder.getOrderId());

		return ResponseEntity.created(location).body(newOrder);     // returns HTTP 201 Created
	}

	private int getUserId(Principal principal) {
		String userName = principal.getName();
		User user = userService.getByUserName(userName);

		return user.getId();
	}
}
