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

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/cart")
@CrossOrigin
public class ShoppingCartController {
	// a shopping cart controller depends on the service layer
	private ShoppingCartService shoppingCartService;
	private UserService userService;        // used to find logged in user db

	// constructor injection tells Spring what objects the controller needs
	public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService) {
		this.shoppingCartService = shoppingCartService;
		this.userService = userService;
	}

	@GetMapping("")
	public ShoppingCart getCart(Principal principal) {
		String userName = principal.getName();      // get the currently logged in username
		User user = userService.getByUserName(userName);        // find database user by username
		int userId = user.getId();      // getting user's database id

		return shoppingCartService.getByUserId(userId);     // gets user's shopping cart from service and returns it
	}

	// add a POST method to add a product to the cart - the url should be
	// https://localhost:8080/cart/products/15  (15 is the productId to be added)
	// return the updated cart with status 201 Created
	@PostMapping("/products/{productId}")
	public ResponseEntity<ShoppingCart> add(@PathVariable int productId, Principal principal) {
		User user = userService.getByUserName(principal.getName());     // gets the logged in user from Principal
		// will either add a new cart item or increase the quantity by 1
		boolean succeeded = shoppingCartService.create(productId, user.getId());

		// returns 400 Bad Request if product doesn't exist
		if(!succeeded) {
			return ResponseEntity.badRequest().build();
		}

		// location tells client where the updated cart can be found
		URI location = URI.create("/cart");
		// reloads cart after adding product *so the response has the updated cart
		var shoppingCart = getCart(principal);

		return ResponseEntity.created(location).body(shoppingCart);     // returns HTTP 201 Created
	}

	// add a PUT method to update an existing product in the cart - the url should be
	// https://localhost:8080/cart/products/15  (15 is the productId to be updated)
	// the BODY should be a ShoppingCartItem - quantity is the only value that will be updated; return the cart (200 OK)
	@PutMapping("/products/{productId}")
	public ResponseEntity<ShoppingCart> update(@PathVariable int productId, @RequestBody ProductQuantity productQuantity, Principal principal) {
		User user = userService.getByUserName(principal.getName());     // only updating logged in user's cart item

		// if qty is less than 1, removes only that item from the cart
		if (productQuantity.quantity < 1) {
			boolean deleted = shoppingCartService.delete(productId, user.getId());     // delete for this user only

			if(!deleted) {
				return ResponseEntity.badRequest().build();     // returns 400 Bad request if item nto found
			}

			// reloading FULL cart after item has been deleted
			ShoppingCart updatedCart = shoppingCartService.getByUserId(user.getId());
			return ResponseEntity.ok(updatedCart);      // returns updated cart with 200 OK
		}

		// updates quantity for the product in logged user's cart
		var item = shoppingCartService.update(productId, user.getId(), productQuantity.quantity);

		// return 400 Bad Request if no item in cart
		if (item == null)
			return ResponseEntity.badRequest().build();

		// reloading FULL cart after item's updated
		ShoppingCart updatedCart = shoppingCartService.getByUserId(user.getId());
		return ResponseEntity.ok(updatedCart);     // returns full cart, similar to POST with HTTP 200 OK
	}

	// add a DELETE method to clear all products from the current users cart
	// https://localhost:8080/cart  - return the (now empty) cart so the front end can refresh it (200 OK)
	@DeleteMapping("")
	public ResponseEntity<ShoppingCart> delete(Principal principal) {
		User user = userService.getByUserName(principal.getName());
        shoppingCartService.delete(user.getId());        // delete all cart rows for user

		// reloads cart after the whole cart has been deleted and will be empty
		ShoppingCart emptyCart = shoppingCartService.getByUserId(user.getId());

		return ResponseEntity.ok(emptyCart);        // returns empty cart with HTTP 200 OK
	}

}
