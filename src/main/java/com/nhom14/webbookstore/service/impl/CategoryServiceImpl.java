package com.nhom14.webbookstore.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.repository.CategoryRepository;
import com.nhom14.webbookstore.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	private CategoryRepository categoryRepository;
	
	@Autowired
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		super();
		this.categoryRepository = categoryRepository;
	}


	@Override
	public List<Category> getActiveCategories() {
		return categoryRepository.findByStatus(1);
	}


	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}


	@Override
	public Category getCategoryById(int id) {
		return categoryRepository.findById(id).orElse(null);
	}


	@Override
	public List<Category> getCategoriesByStatusID(int statusId) {
		return categoryRepository.findByStatus(statusId);
	}


	@Override
	public List<Category> searchCategoriesByKeyword(List<Category> categories, String searchKeyword) {
		List<Category> result = new ArrayList<>();
	    String lowercaseKeyword = searchKeyword.toLowerCase().trim();
	    
	    for (Category category : categories) {
	        if (containsIgnoreCase(Integer.toString(category.getId()), lowercaseKeyword)
	                || containsIgnoreCase(category.getName(), lowercaseKeyword)
	                || containsIgnoreCase(Integer.toString(category.getStatus()), lowercaseKeyword)) {
	            result.add(category);
	        }
	    }

	    return result;
	}
	
	// Kiểm tra xem một chuỗi có chứa một chuỗi con cụ thể hay không,
	// mà không phân biệt chữ hoa chữ thường trong quá trình so sánh
	private boolean containsIgnoreCase(String text, String keyword) {
	    return text.toLowerCase().contains(keyword);
	}


	@Override
	public void updateCategory(Category category) {
		categoryRepository.save(category);
	}


	@Override
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}

	@Override
	public Category getCategoryByName(String categoryName) {
		return categoryRepository.findByName(categoryName);
	}

}
