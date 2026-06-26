package org.yearup.service;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "classpath:test-insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductUpdateTest {
	@Autowired
	private ProductRepository productRepository;
	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository);
	}

	@Test
	void update_shouldSaveUpdatedStock() {
		// arrange
		int productId = 1;

		Product original = productRepository.findById(productId).orElse(null);
		assertNotNull(original);

		Product updatedProduct = getProduct(productId, original);

		// act
		productService.update(productId, updatedProduct);

		// assert
		Product actual = productRepository.findById(productId).orElse(null);

		assertNotNull(actual);
		assertEquals(999, actual.getStock(), "updating a product should save the new stock value.");
	}

	private static @NonNull Product getProduct(int productId, Product original) {
		Product updatedProduct = new Product();
		updatedProduct.setProductId(productId);
		updatedProduct.setName(original.getName());
		updatedProduct.setPrice(original.getPrice());
		updatedProduct.setCategoryId(original.getCategoryId());
		updatedProduct.setDescription(original.getDescription());
		updatedProduct.setImageUrl(original.getImageUrl());
		updatedProduct.setFeatured(original.isFeatured());
		updatedProduct.setSubCategory(original.getSubCategory());

		// this is the field that's testing
		updatedProduct.setStock(999);
		return updatedProduct;
	}
}