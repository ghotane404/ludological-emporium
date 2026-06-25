package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.yearup.models.Category;
import org.yearup.models.Product;
import org.yearup.service.CategoryService;
import org.yearup.service.ProductService;

import java.net.URI;
import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
// http://localhost:8080/categories
// add annotation to allow cross site origin requests
@RestController
@RequestMapping("/categories")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class CategoriesController {
	private CategoryService categoryService;
	private ProductService productService;

	// create an Autowired constructor to inject the categoryService and productService
	@Autowired
	public CategoriesController(CategoryService categoryService, ProductService productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	// add the appropriate annotation for a get action
	@GetMapping("")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Category>> getAll() {
		// find and return all categories
		var categories = categoryService.getAllCategories();

		return ResponseEntity.ok(categories);
	}

	// add the appropriate annotation for a get action
	@GetMapping("{id}")
	@PreAuthorize("permitAll()")
	public ResponseEntity<Category> getById(@PathVariable int id) {
		// get the category by id
		var category = categoryService.getById(id);

		return ResponseEntity.ok(category);
	}

	// the url to return all products in category 1 would look like this
	// https://localhost:8080/categories/1/products
	@GetMapping("{categoryId}/products")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId) {
		// get a list of product by categoryId
		var products = productService.listByCategoryId(categoryId);

		return ResponseEntity.ok(products);
	}

	// add annotation to call this method for a POST action
	// add annotation to ensure that only an ADMIN can call this function
	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		// insert the category and return it with status 201 Created
		var newCategory = categoryService.create(category);
		URI location = URI.create("/categories/" + newCategory.getCategoryId());

		// status 201 created and includes a Location header
		return ResponseEntity.created(location).body(newCategory);
	}

	// add annotation to call this method for a PUT (update) action - the url path must include the categoryId
	// add annotation to ensure that only an ADMIN can call this function
	@PutMapping("{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
		// update the category by id and return the updated category (200 OK)
		var newCategory = categoryService.update(id, category);

		return ResponseEntity.ok(newCategory);
	}

	// add annotation to call this method for a DELETE action - the url path must include the categoryId
	// add annotation to ensure that only an ADMIN can call this function
	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
		// delete the category by id and return status 204 No Content
		categoryService.delete(id);

		return ResponseEntity.noContent().build();
	}
}
