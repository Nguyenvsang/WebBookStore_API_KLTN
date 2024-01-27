package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Book;

public interface BookService {

	// Phương thức lấy sách theo Id
	Book getBookById(int id);
		
	// Phương thức để lấy danh sách tất cả các quyển sách còn kinh doanh
	List<Book> getActiveBooks();

	// Phương thức để lấy danh sách tất cả các quyển sách còn kinh doanh theo danh mục
	List<Book> getActiveBooksByCategory(int categoryId);

	// Phương thức tìm kiếm sách dựa trên từ khóa
	List<Book> searchBooksByKeyword(List<Book> books, String searchKeyword);

	// Phương thức lấy sách còn kinh doanh theo Id
	Book getActiveBookById(int id);

	// Phương thức cập nhật một cuốn sách
	void updateBook(Book book);

	// Phương thức để lấy danh sách tất cả các quyển sách
	List<Book> getAllBooks();

	// Phương thức để lấy những cuốn sách theo danh mục 
	List<Book> getBooksByCategory(Integer categoryId);

	// Phương thức để thêm một quyển sách mới
	void addBook(Book newBook);

	// Lấy quyển sách cuối cùng
	Book getLastBook();

	// Lọc sách theo khoảng giá 
	List<Book> filterBooksByPriceRange(List<Book> books, Double priceMin, Double priceMax);

	// Xếp sách theo giá tăng dần
	List<Book> sortBooksByPriceAscending(List<Book> books);

	// Xếp sách theo giá giảm dần
	List<Book> sortBooksByPriceDescending(List<Book> books);

	// Xếp sách theo chữ cái đầu tiên của tên tăng dần từ A đến Y
	List<Book> sortBooksByNameAscending(List<Book> books);

	// Xếp sách theo chữ cái đầu tiên của tên giảm dần từ Y đến A
	List<Book> sortBooksByNameDescending(List<Book> books);

	// Lọc sách theo tên NXB 
	List<Book> filterBooksByPublisher(List<Book> books, String string);

	// Phương thức để lấy sách theo tên sách
	Book getBookByName(String name);

	// Phương thức để lấy sách theo trạng thái
	List<Book> getBooksByStatus(int status);

}
