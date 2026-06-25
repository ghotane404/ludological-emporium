package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
	private OrderService orderService;
	private UserService userService;

	@Autowired
	public OrdersController(OrderService orderService,  UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@PostMapping("")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<Order> createOrder(Principal principal) {
		User user = userService.getByUserName(principal.getName());

		var newOrder = orderService.create(user.getId());

		if (newOrder == null) {
			return ResponseEntity.badRequest().build();
		}

		URI location = URI.create("/orders" + newOrder.getOrderId());

		return ResponseEntity.created(location).body(newOrder);     // returns HTTP 201 Created
	}



}
