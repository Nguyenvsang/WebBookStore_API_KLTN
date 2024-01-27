package com.nhom14.webbookstore.controller.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Author;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookAuthor;
import com.nhom14.webbookstore.entity.BookImage;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.service.AuthorService;
import com.nhom14.webbookstore.service.BookAuthorService;
import com.nhom14.webbookstore.service.BookImageService;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.CategoryService;
import com.nhom14.webbookstore.service.CloudinaryService;
import com.nhom14.webbookstore.service.BookImageService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AdminBookController {

	private BookService bookService;
	private CategoryService categoryService;
	private CloudinaryService cloudinaryService;
	private BookAuthorService bookAuthorService;
	private AuthorService authorService;
	private BookImageService bookImageService;
	
	@Autowired
	public AdminBookController(BookService bookService, CategoryService categoryService,
			CloudinaryService cloudinaryService, BookAuthorService bookAuthorService,
			AuthorService authorService, BookImageService bookImageService) {
		super();
		this.bookService = bookService;
		this.categoryService = categoryService;
		this.cloudinaryService = cloudinaryService;
		this.bookAuthorService = bookAuthorService;
		this.authorService = authorService;
		this.bookImageService = bookImageService;
	}

	@GetMapping("/managebooks")
	public ResponseEntity<?> manageBooks(@RequestParam(value = "status", required = false) Integer status,
										 @RequestParam(value = "category", required = false) Integer categoryId,
										 @RequestParam(value = "search", required = false) String searchKeyword,
										 @RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
										 @RequestParam(value = "pricemin", required = false) Double priceMin,
										 @RequestParam(value = "pricemax", required = false) Double priceMax,
										 @RequestParam(value = "priceoption", required = false) Integer priceOption,
										 HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		Map<String, Object> response = new HashMap<>();
		List<Book> books;
		int totalBooks;
		// Số sách hiển thị trên mỗi trang
		int recordsPerPage = 10;
		int start;
		int end;
		int totalPages;

		if (status == null) {
			books = bookService.getAllBooks();
			if (books.isEmpty()) {
				return new ResponseEntity<>("Hiện không có sách nào", HttpStatus.NOT_FOUND);
			}
		} else {
			books = bookService.getBooksByStatus(status);
			if (books.isEmpty()) {
				return new ResponseEntity<>("Không tìm thấy sách theo trạng thái này", HttpStatus.NOT_FOUND);
			} else {
				response.put("status", status);
			}

		}

		if (categoryId != null) {
			books = bookService.getBooksByCategory(categoryId);
			if (books.isEmpty()) {
				return new ResponseEntity<>("Không tìm thấy sách theo danh mục này", HttpStatus.NOT_FOUND);
			} else {
				// Thêm để hiển thị theo catagory cho các trang phía sau
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

		totalBooks = books.size();
		start = (currentPage - 1) * recordsPerPage;
		end = Math.min(start + recordsPerPage, totalBooks);

		List<Book> booksOnPage = books.subList(start, end);
		totalPages = (int) Math.ceil((double) totalBooks / recordsPerPage);

		// Tổng số tất cả các đầu sách
		int totalAllBooks = bookService.getAllBooks().size();

		response.put("books", booksOnPage);
		response.put("totalBooks", totalBooks);
		response.put("totalPages", totalPages);
		response.put("currentPage", currentPage);
		List<Category> categories = categoryService.getActiveCategories();
		response.put("categories", categories);
		response.put("totalAllBooks", totalAllBooks);

		// Cuối cùng, trả về ResponseEntity với dữ liệu sách và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/updatestatusbook")
	public ResponseEntity<?> updateStatusBook(@RequestParam("bookId") int bookId,
			@RequestParam("status") int status,
			HttpSession session) {
	    
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}
		
	    // Lấy Book từ bookId 
		Book book = bookService.getBookById(bookId);
	    
	    if (book == null) {
			return new ResponseEntity<>("Không có sách này", HttpStatus.NOT_FOUND);
	    }
	    
	    book.setStatus(status);
	    bookService.updateBook(book);

		return new ResponseEntity<>("Đã cập nhật trạng thái thành công!", HttpStatus.OK);
	}
	
	@GetMapping("/addbook")
	public String showAddBookForm(Model model, 
			HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    // Lấy danh sách tất cả tác giả
        List<Author> allAuthors = authorService.getAllAuthors();
	    
	    List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("allAuthors", allAuthors);
		
		return "admin/addbook";
	}

	@PostMapping("/addbook")
	public ResponseEntity<?> addBook(@ModelAttribute("book") Book bookParam,
									 @RequestParam("image1") MultipartFile image1,
									 @RequestParam("image2") MultipartFile image2,
									 @RequestParam("image3") MultipartFile image3,
									 @RequestParam("image4") MultipartFile image4,
									 @RequestParam("categoryId") Integer categoryId,
									 @RequestParam("authorsIds") List<Integer> authorIds,
									 HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra xem tên sách mới có trùng với tên sách nào trong cơ sở dữ liệu không
		if (bookService.getBookByName(bookParam.getName()) != null) {
			return new ResponseEntity<>("Tên sách đã tồn tại trong cơ sở dữ liệu.", HttpStatus.BAD_REQUEST);
		}

		Book newBook = new Book();

		//Cập nhật thông tin cho cuốn sách mới
		newBook.setName(bookParam.getName());
		newBook.setCostPrice(bookParam.getCostPrice());
		newBook.setSellPrice(bookParam.getSellPrice());
		Category category = categoryService.getCategoryById(categoryId);
		newBook.setCategory(category);
		newBook.setPublisher(bookParam.getPublisher());
		newBook.setDescription(bookParam.getDescription());
		newBook.setStatus(bookParam.getStatus());
		newBook.setDetail(bookParam.getDetail());
		newBook.setQuantity(bookParam.getQuantity());

		// Kiểm tra trường hợp số lượng bằng 0 sẽ đưa về ngừng kinh doanh
		if (newBook.getQuantity() == 0) {
			newBook.setStatus(0);
		}

		// Lưu sách trong cơ sở dữ liệu
		bookService.addBook(newBook);

		// Lấy cuốn sách vừa được lưu để có id
		newBook = bookService.getLastBook();

		// Tạo 4 BookImage mới để chuẩn bị chứa ảnh mới
		BookImage bookImage1 = new BookImage(newBook, "1", 1, "url1");
		BookImage bookImage2 = new BookImage(newBook, "2", 2, "url2");
		BookImage bookImage3 = new BookImage(newBook, "3", 3, "url3");
		BookImage bookImage4 = new BookImage(newBook, "4", 4, "url4");

		//Lưu các BookImage và database
		bookImageService.addBookImage(bookImage1);
		bookImageService.addBookImage(bookImage2);
		bookImageService.addBookImage(bookImage3);
		bookImageService.addBookImage(bookImage4);

		List<BookImage> bookImages = new ArrayList<>();
		bookImages.add(bookImage1);
		bookImages.add(bookImage2);
		bookImages.add(bookImage3);
		bookImages.add(bookImage4);

		boolean updatedImages = updateImages(newBook, bookImages, image1, image2, image3, image4, true);
		// Nếu cập nhật ảnh bị lỗi thì
		if(!updatedImages) {
			return new ResponseEntity<>("Đã xảy ra lỗi khi cập nhật ảnh cho sách.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Phần cập nhật tác giả cho cuốn sách
		List<Author> newAuthors = new ArrayList<>();

		for (Integer authorId : authorIds) {
			Author author = authorService.getAuthorById(authorId);
			if (author != null) {
				newAuthors.add(author);
			}
		}

		// Tạo các bản ghi trong bảng BookAuthor cho tác giả mới
		for (Author author : newAuthors) {
			BookAuthor bookAuthor = new BookAuthor();
			bookAuthor.setBook(newBook);
			bookAuthor.setAuthor(author);
			bookAuthorService.addBookAuthor(bookAuthor);
		}

		return new ResponseEntity<>("Đã thêm sách thành công!", HttpStatus.OK);
	}


	@GetMapping("/managedetailbook")
	public ResponseEntity<?> manageDetailBook(@RequestParam("bookId") Integer bookId, HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Truy xuất dữ liệu từ nguồn dữ liệu
		Book book = bookService.getBookById(bookId);

		List<BookAuthor> bookAuthors = bookAuthorService.getByBook(book);
		String authors = "";
		for (int i = 0; i < bookAuthors.size(); i++) {
			authors += bookAuthors.get(i).getAuthor().getName();
			if (i < bookAuthors.size() - 1) {
				authors += ", ";
			}
		}

		// Tạo một đối tượng Map để chứa tất cả các thuộc tính cần trả về
		Map<String, Object> response = new HashMap<>();
		response.put("book", book);
		response.put("authors", authors);

		// Trả về đối tượng Map dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@GetMapping("/updatebook")
	public String showUpdateBookForm(@RequestParam("bookId") Integer bookId,
			Model model,
			HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    //Lấy sách theo Id
	    Book book = bookService.getBookById(bookId);
	    
        // Lấy danh sách tác giả của cuốn sách
        List<BookAuthor> bookAuthors = bookAuthorService.getByBook(book);
        List<Author> authors = new ArrayList<>();

	     // Lặp qua danh sách bookAuthors và lấy ra tác giả tương ứng
	     for (BookAuthor bookAuthor : bookAuthors) {
	         authors.add(bookAuthor.getAuthor());
	     }
        
        // Lấy danh sách tất cả tác giả
        List<Author> allAuthors = authorService.getAllAuthors();
        
        // Lấy danh sách ảnh của cuốn sách
        
        
        // Đặt thuộc tính vào model để sử dụng trong view
        // Sinh giá trị ngẫu nhiên
        Random random = new Random();
        int randomNumber = random.nextInt();
        model.addAttribute("randomNumber", randomNumber);
	    model.addAttribute("book", book);
	    List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authors);
        model.addAttribute("allAuthors", allAuthors);
        
        return "admin/updatebook";
	}

	@PostMapping("/updatebook")
	public ResponseEntity<?> updateBook(@ModelAttribute("book") Book bookParam,
										@RequestParam("image1") MultipartFile image1,
										@RequestParam("image2") MultipartFile image2,
										@RequestParam("image3") MultipartFile image3,
										@RequestParam("image4") MultipartFile image4,
										@RequestParam("categoryId") Integer categoryId,
										@RequestParam("authorsIds") List<Integer> authorIds,
										HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra xem tên sách cập nhật lại có trùng với tên sách nào trong cơ sở dữ liệu không
		Book existingBook = bookService.getBookByName(bookParam.getName());
		if (existingBook != null && existingBook.getId() != bookParam.getId()) {
			return new ResponseEntity<>("Tên sách đã tồn tại trong cơ sở dữ liệu.", HttpStatus.BAD_REQUEST);
		}

		// Tạo sách để cập nhật
		Book updateBook = bookService.getBookById(bookParam.getId());

		//Cập nhật thông tin cho cuốn sách
		updateBook.setName(bookParam.getName());
		updateBook.setCostPrice(bookParam.getCostPrice());
		updateBook.setSellPrice(bookParam.getSellPrice());
		Category category = categoryService.getCategoryById(categoryId);
		updateBook.setCategory(category);
		updateBook.setPublisher(bookParam.getPublisher());
		updateBook.setDescription(bookParam.getDescription());
		updateBook.setStatus(bookParam.getStatus());
		updateBook.setDetail(bookParam.getDetail());
		updateBook.setQuantity(bookParam.getQuantity());

		// Kiểm tra trường hợp số lượng bằng 0 sẽ đưa về ngừng kinh doanh
		if (updateBook.getQuantity() == 0) {
			updateBook.setStatus(0);
		}

		// Cập nhật sách trong cơ sở dữ liệu
		bookService.updateBook(updateBook);

		// Lấy danh sách BookImage liên quan đến cuốn sách
		List<BookImage> bookImages = bookImageService.getByBook(updateBook);

		boolean updatedImages = updateImages(updateBook, bookImages, image1, image2, image3, image4, true);
		// Nếu cập nhật ảnh bị lỗi thì
		if(!updatedImages) {
			return new ResponseEntity<>("Đã xảy ra lỗi khi cập nhật ảnh cho sách.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Phần cập nhật tác giả cho cuốn sách
		List<Author> newAuthors = new ArrayList<>();

		for (Integer authorId : authorIds) {
			Author author = authorService.getAuthorById(authorId);
			if (author != null) {
				newAuthors.add(author);
			}
		}

		// Xóa các bản ghi cũ trong bảng BookAuthor
		bookAuthorService.deleteBookAuthorsByBook(updateBook);

		// Tạo các bản ghi mới trong bảng BookAuthor cho tác giả mới
		for (Author author : newAuthors) {
			BookAuthor bookAuthor = new BookAuthor();
			bookAuthor.setBook(updateBook);
			bookAuthor.setAuthor(author);
			bookAuthorService.addBookAuthor(bookAuthor);
		}

		return new ResponseEntity<>("Đã cập nhật sách thành công!", HttpStatus.OK);
	}

	private boolean updateImages(Book book, List<BookImage> bookImages, MultipartFile image1, MultipartFile image2,
			MultipartFile image3, MultipartFile image4,
			boolean success) {
		try {
	        // Tạo public ID cho hình ảnh trên Cloudinary (sử dụng id sách)
	        String publicId = "WebBookStoreKLTN/img_book/book_" + book.getId();
	        
	        // Tạo một danh sách các nhiệm vụ tải lên ảnh
	        List<Callable<String>> uploadTasks = new ArrayList<>();
	        
	        // Kiểm tra và tạo nhiệm vụ tải lên ảnh cho từng ảnh
	        if (!image1.isEmpty()) {
	            uploadTasks.add(() -> cloudinaryService.uploadImage(image1, publicId + "/1"));
	        } else {
	            uploadTasks.add(() -> cloudinaryService.uploadImageFromUrl(bookImages.get(0).getPath(), publicId + "/1"));
	        }
	        if (!image2.isEmpty()) {
	            uploadTasks.add(() -> cloudinaryService.uploadImage(image2, publicId + "/2"));
	        } else {
	            uploadTasks.add(() -> cloudinaryService.uploadImageFromUrl(bookImages.get(1).getPath(), publicId + "/2"));
	        }
	        if (!image3.isEmpty()) {
	            uploadTasks.add(() -> cloudinaryService.uploadImage(image3, publicId + "/3"));
	        } else {
	            uploadTasks.add(() -> cloudinaryService.uploadImageFromUrl(bookImages.get(2).getPath(), publicId + "/3"));
	        }
	        if (!image4.isEmpty()) {
	            uploadTasks.add(() -> cloudinaryService.uploadImage(image4, publicId + "/4"));
	        } else {
	            uploadTasks.add(() -> cloudinaryService.uploadImageFromUrl(bookImages.get(3).getPath(), publicId + "/4"));
	        }
	        
	        // Tạo một ExecutorService để thực hiện các nhiệm vụ tải lên ảnh song song
	        ExecutorService executorService = Executors.newFixedThreadPool(uploadTasks.size());
	        
	        // Thực hiện các nhiệm vụ tải lên ảnh và lấy URL của ảnh đã tải lên
	        List<Future<String>> uploadResults = executorService.invokeAll(uploadTasks);
	        int i = 0;
	        for (Future<String> uploadResult : uploadResults) {
	            String imageUrl = uploadResult.get();
	            // Cập nhật URL hình ảnh vào BookImage 
	            bookImages.get(i).setPath(imageUrl);
	            // Lưu vào cơ sở dữ liệu
	            bookImageService.updateBookImage(bookImages.get(i));
	            i++;
	        }
	        
	        // Đóng ExecutorService sau khi hoàn thành
	        executorService.shutdown();
	        return success;
	    } catch (Exception e) {
	        e.printStackTrace();
	        success = false;
	        return success;
	    }
	}

}
