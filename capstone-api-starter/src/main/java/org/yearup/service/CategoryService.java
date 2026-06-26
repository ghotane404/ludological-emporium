package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category getById(int categoryId) {
		// get category by id
		return categoryRepository.findById(categoryId).orElse(null);
	}

	public Category create(Category category) {
		category.setCategoryId(0);  // the id is reset so that it's treated as a new category

		return categoryRepository.save(category);
	}

	public Category update(int categoryId, Category category) {
		// update category and return the updated category
		Category updateCategory = categoryRepository.findById(categoryId).orElse(null);

		if (updateCategory == null) return null;

		updateCategory.setName(category.getName());
		updateCategory.setDescription(category.getDescription());

		return categoryRepository.save(updateCategory);
	}

	public boolean delete(int categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);

		if (category == null) return false;

		categoryRepository.delete(category);

		return true;    // delete category
	}
}
