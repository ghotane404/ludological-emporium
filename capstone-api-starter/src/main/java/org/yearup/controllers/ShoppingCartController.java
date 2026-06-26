package org.yearup.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.dto.ProductQuantity;
import org.yearup.models.ShoppingCart;
import org.yearup.models.User;
import org.yearup.service.ShoppingCartService;
import org.yearup.service.UserService;

import java.net.URI;
import java.security.Principal;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {
	// a shopping cart controller depends on the service layer
	private final ShoppingCartService shoppingCartService;
	private final UserService userService;       // used to find logged in user db

	// constructor injection tells Spring what objects the controller needs
	public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
		this.shoppingCartService = shoppingCartService;
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity <ShoppingCart> getCart(Principal principal) {
		int userId = getUserId(principal);      // getting user's database id
		ShoppingCart shoppingCart = shoppingCartService.getByUserId(userId);

		return ResponseEntity.ok(shoppingCart);    // gets user's shopping cart from service and returns it
	}

	// return the updated cart with status 201 Created
	@PostMapping("/products/{productId}")
	public ResponseEntity<ShoppingCart> addProduct(@PathVariable int productId, Principal principal) {
		int userId = getUserId(principal);
		// will either add a new cart item or increase the quantity by 1
		boolean created = shoppingCartService.create(productId, userId);

		if (!created) return ResponseEntity.badRequest().build();  // returns 400 Bad Request if product doesn't exist

		ShoppingCart updatedCart = shoppingCartService.getByUserId(userId);
		URI location = URI.create("/cart");  // location tells client where the updated cart can be found

		return ResponseEntity.created(location).body(updatedCart);     // returns HTTP 201 Created
	}

	// the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
	@PutMapping("/products/{productId}")
	public ResponseEntity<ShoppingCart> updateProductQuantity(@PathVariable int productId, @RequestBody ProductQuantity productQuantity, Principal principal) {
		int userId = getUserId(principal);
		boolean updated = shoppingCartService.update(productId, userId, productQuantity.quantity);

		if (!updated) return ResponseEntity.badRequest().build();     // returns full cart, similar to POST with HTTP 200 OK

		ShoppingCart updatedCart = shoppingCartService.getByUserId(userId); // reloads FULL cart after item has been updated

		return ResponseEntity.ok(updatedCart);      // returns updated cart with 200 OK
	}

	@DeleteMapping("")
	public ResponseEntity<ShoppingCart> delete(Principal principal) {
		int userId = getUserId(principal);

		shoppingCartService.delete(userId);

		// reloads cart after the whole cart has been deleted and will be empty
		ShoppingCart emptyCart = shoppingCartService.getByUserId(userId);

		return ResponseEntity.ok(emptyCart);        // returns empty cart with HTTP 200 OK
	}

	// helper method to find user id using username
	private int getUserId(Principal principal) {
		String userName = principal.getName();
		User user = userService.getByUserName(userName);

		return user.getId();
	}
}
