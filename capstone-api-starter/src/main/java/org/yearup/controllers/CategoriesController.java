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

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/categories")
@CrossOrigin
public class CategoriesController {
	private final CategoryService categoryService;
	private final ProductService productService;

	@Autowired
	public CategoriesController(CategoryService categoryService, ProductService productService) {
		this.categoryService = categoryService;
		this.productService = productService;
	}

	@GetMapping("")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Category>> getAll() {
		List<Category> categories = categoryService.getAllCategories();

		return ResponseEntity.ok(categories);
	}

	@GetMapping("{id}")
	@PreAuthorize("permitAll()")
	public ResponseEntity<Category> getById(@PathVariable int id) {
		var category = categoryService.getById(id);

		if (category == null) return ResponseEntity.notFound().build();

		return ResponseEntity.ok(category);
	}

	@GetMapping("{categoryId}/products")
	@PreAuthorize("permitAll()")
	public ResponseEntity<List<Product>> getProductsById(@PathVariable int categoryId) {
		var products = productService.listByCategoryId(categoryId);

		return ResponseEntity.ok(products);
	}

	@PostMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Category> addCategory(@RequestBody Category category) {
		var newCategory = categoryService.create(category);
		// location tells clients where the new category can be located
		URI location = URI.create("/categories/" + newCategory.getCategoryId());

		// status 201 created and includes a Location header
		return ResponseEntity.created(location).body(newCategory);
	}

	@PutMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Category> updateCategory(@PathVariable int id, @RequestBody Category category) {
		var updatedCategory = categoryService.update(id, category);

		if (updatedCategory == null) return ResponseEntity.notFound().build();

		return ResponseEntity.ok(updatedCategory);  // return the updated category (200 OK)
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
		boolean deletedCategory = categoryService.delete(id);

		if (!deletedCategory) return ResponseEntity.notFound().build();

		return ResponseEntity.noContent().build();  // return status 204 No Content
	}
}
