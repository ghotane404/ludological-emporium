package org.yearup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yearup.models.CartItem;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {
	private final ShoppingCartRepository shoppingCartRepository;
	private final ProductService productService;

	// constructor injection tells Spring what objects the controller needs
	public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ProductService productService) {
		this.shoppingCartRepository = shoppingCartRepository;
		this.productService = productService;
	}

	public ShoppingCart getByUserId(int userId) {
		// loads the user's cart rows, look up each product
		var items = shoppingCartRepository.findByUserId(userId);
		var shoppingCart = new ShoppingCart();      // empty ShoppingCart object to fill items

		// checking every CartItem from db that belongs to user
		for (var item : items) {
			Product product = productService.getById(item.getProductId());  // use productId to find full Product object
			ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
			shoppingCartItem.setProduct(product);       // sets full product details to shoppingCartItem
			shoppingCartItem.setQuantity(item.getQuantity());       // copy the quantity from the database cart row
			//Add discount???
			shoppingCart.add(shoppingCartItem);     // add them all here
		}

		return shoppingCart;
	}

	// add additional methods here
	public boolean create(int productId, int userId) {
		Product product = productService.getById(productId);        // checking if product exists

		if(product == null)
			return false;       // will fail if it doesn't exist

		// verifying whether user already has product in their cart
		CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

		// if not will create new
		if (cartItem == null) {
			cartItem = new CartItem();      // create a new CartItem entity for the database

			cartItem.setProductId(productId);       // stores which product is being added
			cartItem.setUserId(userId);     // store which user owns this cart item
			cartItem.setQuantity(1);        // setting qty to 1 since it's a new cart
		}
		else {
			// updates qty by 1 if product exists in cart
			cartItem.setQuantity(cartItem.getQuantity() + 1);
		}

		shoppingCartRepository.save(cartItem);      // stores the updated cart to db
		return true;
	}

	public ShoppingCartItem update(int productId, int userId, int quantity) {
		// finding the user and their product
		CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

		// doesn't update if cart doesn't exist
		if(cartItem == null)
			return null;

		cartItem.setQuantity(quantity);     // Update the quantity on the database entity.
		shoppingCartRepository.save(cartItem);      // Save the updated quantity to the database.

		var shoppingCartItem = new ShoppingCartItem();      // Build a ShoppingCartItem response object.
		Product product = productService.getById(productId);    // Get the full product details for the response.

		shoppingCartItem.setProduct(product);       // Add product details to the response object.
		shoppingCartItem.setQuantity(cartItem.getQuantity());       // Add the updated quantity to the response object.

		return shoppingCartItem;
	}

	// No EntityManager with actual transaction available for current thread
	// this means run this method inside a db transaction ?
	@Transactional
	public boolean delete(int userId) {
		shoppingCartRepository.deleteByUserId(userId);    // deletes all cart row for single user
		return true;
	}

	// for the shooping cart when updates qty to less than 1
	@Transactional
	public boolean delete(int productId, int userId) {
		// find the cart item for this user and product
		CartItem cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

		// if the item is not in the cart, delete failed
		if (cartItem == null)
			return false;

		shoppingCartRepository.delete(cartItem);        // delete only this one cart item

		return true;
	}
}
