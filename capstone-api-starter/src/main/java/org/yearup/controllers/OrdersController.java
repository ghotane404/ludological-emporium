package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Order;
import org.yearup.models.User;
import org.yearup.service.OrderService;
import org.yearup.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class OrdersController {
	private OrderService orderService;
	private UserService userService;

	@Autowired
	public OrdersController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("")
	public Order getOrder(Principal principal) {

		return null;
	}

	@PostMapping("")
	public Order createOrder(Principal principal) {

		return null;
	}

}
