package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Category;

public interface CategoryService {

	// Phương thức lấy tất cả các danh mục còn kinh doanh 
	List<Category> getActiveCategories();
	
	// Phương thức lấy tất cả các danh mục 
	List<Category> getAllCategories();

	// Phương thức lấy danh mục theo id
	Category getCategoryById(int id);

	// Lấy danh mục theo trạng thái
	List<Category> getCategoriesByStatusID(int statusId);

	// Lấy danh mục theo từ khóa
	List<Category> searchCategoriesByKeyword(List<Category> categories, String searchKeyword);

	// Cập nhật danh mục
	void updateCategory(Category category);

	// Phương thức để thêm một danh mục mới
	void addCategory(Category category);

	// Phương thức để lấy danh mục theo tên
    Category getCategoryByName(String categoryName);
}
