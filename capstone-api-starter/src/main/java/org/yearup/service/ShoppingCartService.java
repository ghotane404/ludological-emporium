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
		var cartItems = shoppingCartRepository.findByUserId(userId);    // loads the user's cart rows, look up each product
		var shoppingCart = new ShoppingCart();      // empty ShoppingCart object to fill in shoppingCartItem

		// checking every item in cartItems from db that belongs to user
		for (var item : cartItems) {
			var product = productService.getById(item.getProductId());  // use productId to find full Product object
			// ShoppingCartItem object holds project details and qty before adding it to shoppingCart
			var shoppingCartItem = new ShoppingCartItem();

			shoppingCartItem.setProduct(product);       // sets full product details to shoppingCartItem
			shoppingCartItem.setQuantity(item.getQuantity());       // copy the quantity from the database cart row

			shoppingCart.add(shoppingCartItem);     // add all the shoppingCartItem in the shoppingCart object
		}

		return shoppingCart;
	}

	public boolean create(int productId, int userId) {
		var product = productService.getById(productId);        // checking if product exists

		if(product == null) return false;       // will fail if it doesn't exist

		// verifying whether user already has product in their cart 
		var cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

		if (cartItem == null) {
			cartItem = new CartItem();      // create a new CartItem entity for the database

			cartItem.setProductId(productId);       // stores which product is being added
			cartItem.setUserId(userId);     // store which user owns this cart item
			cartItem.setQuantity(1);        // setting qty to 1 since it's a new cart
		}
		else {
			cartItem.setQuantity(cartItem.getQuantity() + 1);   // updates qty by 1 if product exists in cart
		}

		shoppingCartRepository.save(cartItem);      // stores the updated cart to db

		return true;
	}

	@Transactional
	public boolean update(int productId, int userId, int quantity) {
		var cartItem = shoppingCartRepository.findByUserIdAndProductId(userId, productId);

		if(cartItem == null) return false;      // doesn't update if cart doesn't exist

		// if qty is less than 1, removes only that item from the cart
		if (quantity < 1){
			shoppingCartRepository.delete(cartItem);       // delete for this user only
			return true;
		}

		cartItem.setQuantity(quantity);     // Update the quantity on the database entity.
		shoppingCartRepository.save(cartItem);      // Save the updated quantity to the database.

		return true;
	}

	// No EntityManager with actual transaction available for current thread
	// this means run this method inside a db transaction ?
	@Transactional
	public boolean delete(int userId) {
		shoppingCartRepository.deleteByUserId(userId);    // deletes all cart row for single user

		return true;
	}
}
