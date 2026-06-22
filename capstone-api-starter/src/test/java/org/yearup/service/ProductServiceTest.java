package org.yearup.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.yearup.models.Product;
import org.yearup.repository.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
		assertEquals(10, products.size());
	}

	@Test
	void findByCategoryId_shouldReturnProductsInCategory() {
		// act
		List<Product> products = productRepository.findByCategoryId(1);

		// assert
		assertEquals(4, products.size());
	}
}