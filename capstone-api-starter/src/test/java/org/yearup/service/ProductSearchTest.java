package org.yearup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql(scripts = "classpath:test-insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductSearchTest {

	@Autowired
	private ProductRepository productRepository;
	private ProductService productService;

	@BeforeEach
	void setUp() {
		productService = new ProductService(productRepository);
	}

	@Test
	void search_shouldReturnOnlyCategoryOneProducts_whenCatIsOne() {
		// act
		List<Product> products = productService.search(1, null, null, null);

		// assert
		assertEquals(3, products.size());

		for (Product product : products) {
			assertEquals(1, product.getCategoryId());
		}
	}

	@Test
	void search_shouldReturnOnlyProductsAtOrAboveMinPrice() {
		// act
		List<Product> products = productService.search(null, 25.00, null, null);

		// assert
		assertFalse(products.isEmpty());

		for (Product product : products) {
			assertTrue(product.getPrice() >= 25.00);
		}
	}

	@Test
	void search_shouldReturnOnlyProductsAtOrBelowMaxPrice() {
		// act
		List<Product> products = productService.search(null, null, 30.00, null);

		// assert
		assertFalse(products.isEmpty());

		for (Product product : products) {
			assertTrue(product.getPrice() <= 30.00);
		}
	}

	@Test
	void search_shouldReturnProductsWithinMinAndMaxPriceRange() {
		// act
		List<Product> products = productService.search(null, 25.00, 60.00, null);

		// assert
		assertFalse(products.isEmpty());

		for (Product product : products) {
			assertTrue(product.getPrice() >= 25.00);
			assertTrue(product.getPrice() <= 60.00);
		}
	}

	@Test
	void search_shouldReturnOnlyProductsWithMatchingSubCategory() {
		// act
		List<Product> products = productService.search(null, null, null, "RPG");

		// assert
		assertEquals(2, products.size());

		for (Product product : products) {
			assertEquals("RPG", product.getSubCategory());
		}
	}

	@Test
	void search_shouldReturnProductsMatchingCategoryAndSubCategory() {
		// act
		List<Product> products = productService.search(2, null, null, "RPG");

		// assert
		assertEquals(2, products.size());

		for (Product product : products) {
			assertEquals(2, product.getCategoryId());
			assertEquals("RPG", product.getSubCategory());
		}
	}

	@Test
	void search_shouldReturnProductsMatchingCategoryMinPriceMaxPriceAndSubCategory() {
		// act
		List<Product> products = productService.search(2, 20.00, 60.00, "RPG");

		// assert
		assertEquals(2, products.size());

		for (Product product : products) {
			assertEquals(2, product.getCategoryId());
			assertTrue(product.getPrice() >= 20.00);
			assertTrue(product.getPrice() <= 60.00);
			assertEquals("RPG", product.getSubCategory());
		}
	}
}