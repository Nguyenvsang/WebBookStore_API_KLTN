package com.nhom14.webbookstore.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AdminCategoryController {
	private CategoryService categoryService;
	private BookService bookService;
	
	@Autowired
	public AdminCategoryController(CategoryService categoryService, BookService bookService) {
		super();
		this.categoryService = categoryService;
		this.bookService = bookService;
	}

	@GetMapping("/managecategories")
	public ResponseEntity<?> manageCategory(@RequestParam(value = "status", required = false) Integer statusId,
											@RequestParam(value = "search", required = false) String searchKeyword,
											@RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
											HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		List<Category> categories;
		int totalCategories;

		// Số danh mục hiển thị trên mỗi trang
		int recordsPerPage = 10;
		int start;
		int end;
		int totalPages;

		if (statusId == null || (statusId == -1)) {
			categories = categoryService.getAllCategories();
		} else {
			categories = categoryService.getCategoriesByStatusID(statusId);
		}

		if (searchKeyword != null && !searchKeyword.isEmpty()) {
			categories = categoryService.searchCategoriesByKeyword(categories, searchKeyword);
		}

		// Lấy tổng số lượng danh mục
		totalCategories = categories.size();

		// Tính toán vị trí bắt đầu và kết thúc của danh mục trên trang hiện tại
		start = (currentPage - 1) * recordsPerPage;
		end = Math.min(start + recordsPerPage, totalCategories);

		// Lấy danh sách danh mục trên trang hiện tại
		List<Category> categoriesOnPage = categories.subList(start, end);

		// Tính toán số trang
		totalPages = (int) Math.ceil((double) totalCategories / recordsPerPage);

		// Tổng số tất cả các danh mục
		int totalAllCategories = categoryService.getAllCategories().size();

		// Tạo một đối tượng Map để chứa tất cả các thuộc tính cần trả về
		Map<String, Object> response = new HashMap<>();
		response.put("categories", categoriesOnPage);
		response.put("totalCategories", totalCategories);
		response.put("totalPages", totalPages);
		response.put("currentPage", currentPage);
		response.put("totalAllCategories", totalAllCategories);

		// Trả về đối tượng Map dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/updatestatuscategory")
	public ResponseEntity<?> updateStatusCategory(@RequestParam("categoryId") int categoryId,
			@RequestParam("status") int status,
			HttpSession session) {
	    
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}
		
	    // Lấy Category từ categoryId 
		Category category = categoryService.getCategoryById(categoryId);
	    
	    if (category == null) {
			return new ResponseEntity<>("Không tìm thấy danh mục", HttpStatus.BAD_REQUEST);
	    }
	    
	    // Kiểm tra nếu trạng thái mới của danh mục là 0 và đã được lưu thành công vào cơ sở dữ liệu
        // Thì cập nhật trạng thái của các cuốn sách thuộc danh mục này thành 0
	    category.setStatus(status);
        categoryService.updateCategory(category);
	    if (status == 0) {
            // Cập nhật trạng thái của các cuốn sách thuộc danh mục thành 0
            List<Book> booksInCategory = bookService.getBooksByCategory(categoryId);
            for (Book book : booksInCategory) {
                book.setStatus(status);
                bookService.updateBook(book);
            }
	    }

		return new ResponseEntity<>("Đã cập nhật trạng thái thành công!", HttpStatus.OK);
	}
	
	@GetMapping("/updatecategory")
	public String showUpdateCategoryForm(@RequestParam("categoryId") Integer categoryId,
			Model model,
			HttpSession session) {
		
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    // Lấy category từ categoryId
        Category category = categoryService.getCategoryById(categoryId);
        
        // Đặt thuộc tính vào model để sử dụng trong view
	    model.addAttribute("category", category);
	    
	    return "admin/updatecategory";
	    
	}

	@PostMapping("/updatecategory")
	public ResponseEntity<?> updateCategory(
			@RequestParam("id") int categoryId,
			@RequestParam("name") String categoryName,
			@RequestParam("status") int status,
			HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra xem tên danh mục cập nhật lại có trùng với tên danh mục nào trong cơ sở dữ liệu không
		Category existingCategory = categoryService.getCategoryByName(categoryName);
		if (existingCategory != null && existingCategory.getId() != categoryId) {
			return new ResponseEntity<>("Tên danh mục đã tồn tại trong cơ sở dữ liệu.", HttpStatus.BAD_REQUEST);
		}

		// Lấy category từ categoryId
		Category category = categoryService.getCategoryById(categoryId);

		if (category != null) {
			// Kiểm tra nếu trạng thái mới của danh mục là 0 và đã được lưu thành công vào cơ sở dữ liệu
			// Thì cập nhật trạng thái của các cuốn sách thuộc danh mục này thành 0
			if (status == 0) {
				category.setName(categoryName);
				category.setStatus(status);
				categoryService.updateCategory(category);

				// Cập nhật trạng thái của các cuốn sách thuộc danh mục thành 0
				List<Book> booksInCategory = bookService.getBooksByCategory(categoryId);
				for (Book book : booksInCategory) {
					book.setStatus(status);
					bookService.updateBook(book);
				}
			} else {
				// Nếu trạng thái mới của danh mục không phải là 0, chỉ cập nhật thông tin của danh mục
				category.setName(categoryName);
				category.setStatus(status);
				categoryService.updateCategory(category);
			}

			// Trả về thông báo thành công và mã trạng thái OK
			return new ResponseEntity<>("Cập nhật thành công", HttpStatus.OK);
		} else {
			// Trả về thông báo lỗi và mã trạng thái NOT_FOUND
			return new ResponseEntity<>("Không tìm thấy danh mục", HttpStatus.NOT_FOUND);
		}
	}


	@GetMapping("/addcategory")
	public String showAddCategoryForm(HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    return "admin/addcategory";
	}

	@PostMapping("/addcategory")
	public ResponseEntity<?> addCategory(@RequestParam("name") String categoryName,
										 @RequestParam("status") int status,
										 HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra xem tên danh mục mới có trùng với tên nào trong cơ sở dữ liệu không
		if (categoryService.getCategoryByName(categoryName) != null) {
			return new ResponseEntity<>("Tên danh mục đã tồn tại trong cơ sở dữ liệu.", HttpStatus.BAD_REQUEST);
		}

		// Tạo Category mới
		Category category = new Category(categoryName, status);

		// Gọi phương thức addCategory từ service để thêm mới danh mục
		categoryService.addCategory(category);

		// Trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Đã thêm danh mục mới thành công", HttpStatus.OK);
	}

}
