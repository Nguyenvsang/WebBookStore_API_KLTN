package com.nhom14.webbookstore.controller.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.CategoryService;

@RestController
@CrossOrigin
public class BookController {
	private BookService bookService;
	private CategoryService categoryService;

	@Autowired
	public BookController(BookService bookService, CategoryService categoryService) {
		super();
		this.bookService = bookService;
		this.categoryService = categoryService;
	}

    @GetMapping("/viewbooks")
    public ResponseEntity<?> viewBooks(@RequestParam(value = "category", required = false) Integer categoryId,
                                       @RequestParam(value = "search", required = false) String searchKeyword,
                                       @RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
                                       @RequestParam(value = "pricemin", required = false) Double priceMin,
                                       @RequestParam(value = "pricemax", required = false) Double priceMax,
                                       @RequestParam(value = "priceoption", required = false) Integer priceOption,
                                       @RequestParam(value = "nameoption", required = false) Integer nameOption,
                                       @RequestParam(value = "publisher", required = false) Integer publisher) {
        Map<String, Object> response = new HashMap<>();
        List<Book> books;
        int totalBooks;
        int recordsPerPage = 12;
        int start;
        int end;
        int totalPages;

        if (categoryId == null) {
            books = bookService.getActiveBooks();
            if (books.isEmpty()) {
                return new ResponseEntity<>("Hiện không có sách nào được bán, vui lòng quay lại sau", HttpStatus.NOT_FOUND);
            }
        } else {
            books = bookService.getActiveBooksByCategory(categoryId);
            if (books.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sách theo danh mục này", HttpStatus.NOT_FOUND);
            }
            else {
                response.put("categoryId", categoryId);
            }
        }

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            books = bookService.searchBooksByKeyword(books, searchKeyword);
            if (books.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sách nào với từ khóa đã nhập", HttpStatus.NOT_FOUND);
            }
            else {
                response.put("search", searchKeyword);
            }
        }

        // Lọc sách theo tên nhà sản xuất
        if(publisher != null) {
            books = filterBooksByPublisher(books, publisher);
            if (books.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sách theo nhà xuất bản này", HttpStatus.NOT_FOUND);
            } else { //Thêm để hiển thị theo NXB cho các trang phía sau
                response.put("publisher", publisher);
            }
        }

        // Lọc sách theo khoảng giá
        if (priceMin != null && priceMax != null) {
            books = bookService.filterBooksByPriceRange(books, priceMin, priceMax);
            if (books.isEmpty()) {
                return new ResponseEntity<>("Không tìm thấy sách nào trong khoảng giá đã chọn", HttpStatus.NOT_FOUND);
            } else { //Thêm để hiển thị theo khoảng giá cho các trang phía sau
                response.put("pricemin", priceMin);
                response.put("pricemax", priceMax);
            }
        }

        // Sếp sách tăng dần nếu giá trị priceOption là 12, giảm dần nếu giá trị là 21
        if (priceOption != null) {
            if (priceOption == 12) {
                books = bookService.sortBooksByPriceAscending(books);
                //Thêm để hiển thị theo khoảng giá cho các trang phía sau
                response.put("priceoption", priceOption);
            } else if (priceOption == 21) {
                books = bookService.sortBooksByPriceDescending(books);
                //Thêm để hiển thị theo khoảng giá cho các trang phía sau
                response.put("priceoption", priceOption);
            }
        }

        // Xếp sách theo chữ cái đầu tiên của tên sách tăng dần từ A đến Y nếu nameOption là 12 và ngược lại
        if (nameOption != null) {
            if (nameOption == 12) {
                books = bookService.sortBooksByNameAscending(books);
                //Thêm để hiển thị theo khoảng giá cho các trang phía sau
                response.put("nameoption", nameOption);
            } else if (nameOption == 21) {
                books = bookService.sortBooksByNameDescending(books);
                //Thêm để hiển thị theo khoảng giá cho các trang phía sau
                response.put("nameoption", nameOption);
            }
        }

        totalBooks = books.size();
        start = (currentPage - 1) * recordsPerPage;
        end = Math.min(start + recordsPerPage, totalBooks);

        List<Book> booksOnPage = books.subList(start, end);
        totalPages = (int) Math.ceil((double) totalBooks / recordsPerPage);


        response.put("books", booksOnPage);
        response.put("totalBooks", totalBooks);
        response.put("totalPages", totalPages);
        response.put("currentPage", currentPage);
        List<Category> categories = categoryService.getActiveCategories();
        response.put("categories", categories);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private List<Book> filterBooksByPublisher(List<Book> books, Integer publisher) {
		if (publisher == 1) {
            books = bookService.filterBooksByPublisher(books, "NXB Kim Đồng");
        }
		if (publisher == 2) {
            books = bookService.filterBooksByPublisher(books, "NXB Lao Động");
        }
		if (publisher == 3) {
            books = bookService.filterBooksByPublisher(books, "NXB Thế Giới");
        }
		if (publisher == 4) {
            books = bookService.filterBooksByPublisher(books, "NXB Trẻ");
        }
		if (publisher == 5) {
            books = bookService.filterBooksByPublisher(books, "NXB Thanh Niên");
        }
		if (publisher == 6) {
            books = bookService.filterBooksByPublisher(books, "NXB Hồng Đức");
        }
		if (publisher == 7) {
            books = bookService.filterBooksByPublisher(books, "NXB Chính Trị Quốc Gia");
        }
		if (publisher == 8) {
            books = bookService.filterBooksByPublisher(books, "NXB Văn Học");
        }
		if (publisher == 9) {
            books = bookService.filterBooksByPublisher(books, "NXB Hội Nhà Văn");
        }
		if (publisher == 10) {
            books = bookService.filterBooksByPublisher(books, "NXB Dân Trí");
        }
		
		return books;
	}

    @GetMapping("/detailbook/{id}")
    public ResponseEntity<?> viewDetailBook(@PathVariable Integer id) {
        // Lấy thông tin về cuốn sách từ id
        Book book = bookService.getActiveBookById(id);

        // Lấy danh sách các danh mục
        List<Category> categories = categoryService.getActiveCategories();

        // Tạo một Map để chứa thông tin sách và danh mục
        Map<String, Object> response = new HashMap<>();
        response.put("book", book);
        response.put("categories", categories);

        // Trả về Map và mã trạng thái OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
