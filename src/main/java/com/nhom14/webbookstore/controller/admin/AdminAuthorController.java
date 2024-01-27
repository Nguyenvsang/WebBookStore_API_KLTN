package com.nhom14.webbookstore.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Author;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.service.AuthorService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AdminAuthorController {

	private AuthorService authorService;

	@Autowired
	public AdminAuthorController(AuthorService authorService) {
		super();
		this.authorService = authorService;
	}

	@GetMapping("/manageauthors")
	public ResponseEntity<?> manageAuthors(@RequestParam(value = "search", required = false) String searchKeyword,
										   @RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
										   HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		List<Author> authors = authorService.getAllAuthors();
		int totalAuthors;
		// Số tác giả hiển thị trên mỗi trang
		int recordsPerPage = 10;
		int start;
		int end;
		int totalPages;

		if (searchKeyword != null && !searchKeyword.isEmpty()) {
			authors = authorService.searchAuthorsByKeyword(authors, searchKeyword);
		}

		totalAuthors = authors.size();

		// Tính toán vị trí bắt đầu và kết thúc của tác giả trên trang hiện tại
		start = (currentPage - 1) * recordsPerPage;
		end = Math.min(start + recordsPerPage, totalAuthors);

		// Lấy danh sách tác giả trên trang hiện tại
		List<Author> authorsOnPage = authors.subList(start, end);

		// Tính toán số trang
		totalPages = (int) Math.ceil((double) totalAuthors / recordsPerPage);

		// Tổng số tất cả các tác giả
		int totalAllAuthors = authorService.getAllAuthors().size();

		// Tạo một đối tượng Map để chứa tất cả các thuộc tính cần trả về
		Map<String, Object> response = new HashMap<>();
		response.put("authors", authorsOnPage);
		response.put("totalAuthors", totalAuthors);
		response.put("totalPages", totalPages);
		response.put("currentPage", currentPage);
		response.put("totalAllAuthors", totalAllAuthors);

		// Trả về đối tượng Map dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@GetMapping("/addauthor")
	public String showAddAuthorForm(Model model, 
			HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
		return "admin/addauthor";
	}

	@PostMapping("/addauthor")
	public ResponseEntity<?> addAuthor(@RequestParam("name") String authorName,
									   @RequestParam("bio") String authorBio,
									   HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra xem tên tác giả đã tồn tại trong cơ sở dữ liệu hay chưa
		Author existingAuthor = authorService.findAuthorByName(authorName);
		if (existingAuthor != null) {
			// Nếu tên tác giả đã tồn tại, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Đã tồn tại tác giả này trong cơ sở dữ liệu", HttpStatus.BAD_REQUEST);
		}

		// Tạo Author mới
		Author author = new Author(authorName, authorBio);

		// Gọi phương thức addAuthor từ service để thêm mới tác giả
		authorService.addAuthor(author);

		// Trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Đã thêm tác giả mới thành công", HttpStatus.OK);
	}


	@GetMapping("/managedetailauthor")
	public ResponseEntity<?> manageDetailAuthor(@RequestParam("authorId") Integer authorId, HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Truy xuất dữ liệu từ nguồn dữ liệu
		Author author = authorService.getAuthorById(authorId);

		// Trả về đối tượng Author dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(author, HttpStatus.OK);
	}

	@GetMapping("/updateauthor")
	public String showUpdateAuthorForm(@RequestParam("authorId") Integer authorId,
			Model model,
			HttpSession session) {
		
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    // Lấy author từ authorId
        Author author = authorService.getAuthorById(authorId);
        
        // Đặt thuộc tính vào model để sử dụng trong view
	    model.addAttribute("author", author);
	    
	    return "admin/updateauthor";
	    
	}

	@PostMapping("/updateauthor")
	public ResponseEntity<?> updateAuthor(@RequestParam("id") Integer authorId,
										  @RequestParam("name") String authorName,
										  @RequestParam("bio") String bio,
										  HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Lấy author từ authorId
		Author author = authorService.getAuthorById(authorId);

		if (author != null) {
			// Kiểm tra xem tên tác giả mới có trùng với tên của bất kỳ tác giả hiện có nào không
			Author existingAuthor = authorService.findAuthorByName(authorName);
			if (existingAuthor != null && existingAuthor.getId() != authorId) {
				// Nếu tên tác giả mới trùng với tên của một tác giả hiện có khác, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
				return new ResponseEntity<>("Tên tác giả đã tồn tại trong cơ sở dữ liệu", HttpStatus.BAD_REQUEST);
			}

			author.setName(authorName);
			author.setBio(bio);
			authorService.updateAuthor(author);
			// Trả về thông báo thành công và mã trạng thái OK
			return new ResponseEntity<>("Cập nhật thành công", HttpStatus.OK);
		} else {
			// Trả về thông báo lỗi và mã trạng thái NOT_FOUND
			return new ResponseEntity<>("Không tìm thấy tác giả", HttpStatus.NOT_FOUND);
		}
	}

}
