package org.yearup.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Sql(scripts = "classpath:test-insert-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProductRepositoryTest {
	@Autowired
	private ProductRepository productRepository;

	@Test
	void findAll_shouldReturnAllProductsFromDatabase() {
		// act
		List<Product> products = productRepository.findAll();

		// assert
		assertEquals(8, products.size());
	}

	@Test
	void findByCategoryId_shouldReturnProductsInCategory() {
		// act
		List<Product> products = productRepository.findByCategoryId(1);

		// assert
		assertEquals(3, products.size());
	}

	@Test
	public void getById_shouldReturn_theCorrectProduct() {
		// arrange
		int productId = 1;

		// act
		Product actual = productRepository.findById(productId).orElse(null);

		// assert
		assertNotNull(actual, "Because product 1 should exist in the test database.");
		assertEquals("The Legend of Zelda: Breath of the Wild", actual.getName());
		assertEquals(59.99, actual.getPrice(), 0.001, "Because product 1 is Zelda in the test database.");
	}
}
