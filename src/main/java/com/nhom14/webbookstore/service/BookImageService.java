package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookImage;

public interface BookImageService {

	List<BookImage> getByBook(Book book);
	
	// Phương thức để cập nhật hình ảnh
	void updateBookImage(BookImage bookImage);

	// Phương thức để thêm một sách - hình ảnh mới
	void addBookImage(BookImage bookImage);

}
