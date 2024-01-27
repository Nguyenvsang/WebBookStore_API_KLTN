package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Author;

public interface AuthorService {

	// Lấy danh sách tất cả tác giả
	List<Author> getAllAuthors();

	// Lấy tác giả theo mã
	Author getAuthorById(int id);

	// Phương thức tìm kiếm tác giả dựa trên từ khóa
	List<Author> searchAuthorsByKeyword(List<Author> authors, String searchKeyword);

	// Phương thức để thêm một tác giả mới
	void addAuthor(Author author);

	// Cập nhật tác giả
	void updateAuthor(Author author);

	// Tìm tác giả bằng Tên
	Author findAuthorByName(String authorName);
}
