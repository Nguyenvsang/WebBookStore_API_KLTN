package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookAuthor;

public interface BookAuthorService {

	// Lấy danh sách tác giả - sách theo sách 
	List<BookAuthor> getByBook(Book book);

	// Xóa tác giả - sách theo sách
	void deleteBookAuthorsByBook(Book book);

	// Phương thức để thêm một tác giả - sách mới 
	void addBookAuthor(BookAuthor bookAuthor);

}
