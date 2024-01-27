package com.nhom14.webbookstore.controller.admin;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.service.AccountService;
import com.nhom14.webbookstore.service.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.*;

@RestController
@CrossOrigin
public class AdminAccountController {
	private AccountService accountService;
	private CloudinaryService cloudinaryService;

	@Autowired
	public AdminAccountController(AccountService accountService, CloudinaryService cloudinaryService) {
		super();
		this.accountService = accountService;
		this.cloudinaryService = cloudinaryService;
	}
	
	@GetMapping("/loginadmin")
	public String loginAdminForm() {
		return "admin/loginadmin";
	}

	@PostMapping("/loginadmin")
	public ResponseEntity<?> loginAccount(@RequestParam("username") String username,
										  @RequestParam("password") String password,
										  HttpSession session,
										  HttpServletRequest request) {
		session.invalidate();
		session = request.getSession(true);
		// Kiểm tra đăng nhập bằng phương thức checkLogin
		boolean isValid = accountService.checkLoginAdmin(username, password);

		if (isValid) {
			// Nếu đăng nhập thành công, lưu thông tin admin vào session
			Account admin = accountService.findAccountByUsername(username);
			session.setAttribute("admin", admin);
			// Nếu đăng nhập thành công, trả về Account và mã trạng thái OK
			return new ResponseEntity<>(admin, HttpStatus.OK);
		} else {
			// Nếu đăng nhập thất bại, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Sai tên đăng nhập hoặc mật khẩu", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/logoutadmin")
	public String logoutAdminForm() {
		return "admin/logoutadmin";
	}

	@PostMapping("/logoutadmin")
	public ResponseEntity<?> logoutAdmin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		// Trả về mã trạng thái OK
		return new ResponseEntity<>("Đã đăng xuất", HttpStatus.OK);
	}

	@GetMapping("/manageaccounts")
	public ResponseEntity<?> manageAccounts(@RequestParam(value = "status", required = false) Integer status,
											@RequestParam(value = "search", required = false) String searchKeyword,
											@RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
											HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Lấy danh sách tài khoản theo trạng thái hoặc tất cả tài khoản
		List<Account> accounts;
		int totalAccounts;

		// Số danh mục hiển thị trên mỗi trang
		int recordsPerPage = 10;
		int start;
		int end;
		int totalPages;

		if (status == null || (status == -1)) {
			accounts = accountService.getAllAccounts();
		} else {
			accounts = accountService.getAccountsByStatus(status);
		}

		if (searchKeyword != null && !searchKeyword.isEmpty()) {
			accounts = accountService.searchAccountsByKeyword(accounts, searchKeyword);
		}

		// Lấy tổng số lượng tài khoản
		totalAccounts = accounts.size();

		// Tính toán vị trí bắt đầu và kết thúc của tài khoản trên trang hiện tại
		start = (currentPage - 1) * recordsPerPage;
		end = Math.min(start + recordsPerPage, totalAccounts);

		// Lấy danh sách tài khoản trên trang hiện tại
		List<Account> accountsOnPage = accounts.subList(start, end);

		// Tính toán số trang
		totalPages = (int) Math.ceil((double) totalAccounts / recordsPerPage);

		// Tổng số tất cả các tài khoản
		int totalAllAccounts = accountService.getAllAccounts().size();

		// Tạo một Map để chứa thông tin tài khoản và các thông tin khác
		Map<String, Object> response = new HashMap<>();
		response.put("accounts", accountsOnPage);
		response.put("totalAccounts", totalAccounts);
		response.put("totalPages", totalPages);
		response.put("currentPage", currentPage);
		response.put("totalAllAccounts", totalAllAccounts);

		// Trả về Map và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/managedetailaccount")
	public ResponseEntity<?> manageDetailAccount(@RequestParam("accountId") Integer accountId, HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Truy xuất dữ liệu từ nguồn dữ liệu
		Account account = accountService.getAccountById(accountId);

		// Sinh giá trị ngẫu nhiên
		Random random = new Random();
		int randomNumber = random.nextInt();

		// Tạo một Map để chứa thông tin tài khoản và số ngẫu nhiên
		Map<String, Object> response = new HashMap<>();
		response.put("account", account);
		response.put("randomNumber", randomNumber);

		// Trả về Map và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/editaccount")
	public String editAccountForm(@RequestParam("accountId") Integer accountId,
			Model model,
		    HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    // Truy xuất dữ liệu từ nguồn dữ liệu 
        Account account = accountService.getAccountById(accountId);
        
        // Lưu thông tin tài khoản vào model
        model.addAttribute("account", account);
        // Sinh giá trị ngẫu nhiên
        Random random = new Random();
        int randomNumber = random.nextInt();
        model.addAttribute("randomNumber", randomNumber);

        // Forward đến trang xem sửa tài khoản
        return "admin/editaccount";
	}

	@PostMapping("/editaccount")
	public ResponseEntity<?> editAccount(@ModelAttribute("account") Account accountParam,
										 @RequestParam("image") MultipartFile image,
										 @RequestParam("dob") String dob,
										 HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		Account updateAccount = accountService.getAccountById(accountParam.getId());

		// Kiểm tra xem username, sdt, email mới có bị trùng với của người khác không
		Account existingAccount = null;
		if (!Objects.equals(updateAccount.getUsername(), accountParam.getUsername())) {
			existingAccount = accountService.findAccountByUsername(accountParam.getUsername());
			if (existingAccount != null) {
				return new ResponseEntity<>("Tên tài khoản đã tồn tại. Vui lòng nhập giá trị khác.", HttpStatus.BAD_REQUEST);
			}
		}

		if (!Objects.equals(updateAccount.getPhoneNumber(), accountParam.getPhoneNumber())) {
			existingAccount = accountService.findAccountByPhoneNumber(accountParam.getPhoneNumber());
			if (existingAccount != null) {
				return new ResponseEntity<>("Số điện thoại đã tồn tại. Vui lòng nhập giá trị khác.", HttpStatus.BAD_REQUEST);
			}
		}

		if (!Objects.equals(updateAccount.getEmail(), accountParam.getEmail())) {
			existingAccount = accountService.findAccountByEmail(accountParam.getEmail());
			if (existingAccount != null) {
				return new ResponseEntity<>("Email đã tồn tại. Vui lòng nhập giá trị khác.", HttpStatus.BAD_REQUEST);
			}
		}

		try {
			if (!image.isEmpty()) {
				// Tạo public ID cho hình ảnh trên Cloudinary (sử dụng id người dùng)
				String publicId = "WebBookStoreKLTN/img_account/account_" + updateAccount.getId();

				// Tải lên hình ảnh lên Cloudinary và lấy URL
				String imageUrl = cloudinaryService.uploadImage(image, publicId);

				// Cập nhật URL hình ảnh vào tài khoản
				updateAccount.setImg(imageUrl);
			}

			// Cập nhật thông tin tài khoản
			updateAccount.setUsername(accountParam.getUsername());
			updateAccount.setFirstName(accountParam.getFirstName());
			updateAccount.setLastName(accountParam.getLastName());
			updateAccount.setGender(accountParam.getGender());
			updateAccount.setDateOfBirth(Date.valueOf(dob));
			updateAccount.setAddress(accountParam.getAddress());
			updateAccount.setPhoneNumber(accountParam.getPhoneNumber());
			updateAccount.setEmail(accountParam.getEmail());
			updateAccount.setStatus(accountParam.getStatus());
			accountService.updateAccount(updateAccount);

			// Trả về thông báo thành công và mã trạng thái OK
			return new ResponseEntity<>("Đã cập nhật thành công!", HttpStatus.OK);
		} catch (IOException e) {
			// Trả về thông báo lỗi và mã trạng thái INTERNAL_SERVER_ERROR
			return new ResponseEntity<>("Đã xảy ra lỗi khi cập nhật tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/managechangepassword")
	public String manageChangePasswordForm(@RequestParam("accountId") Integer accountId,
			Model model,
		    HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

	    // Kiểm tra xem admin đã đăng nhập hay chưa
	    if (admin == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/loginadmin";
	    }
	    
	    // Truy xuất dữ liệu từ nguồn dữ liệu 
        Account account = accountService.getAccountById(accountId);
        
        // Lưu thông tin tài khoản vào model
        model.addAttribute("account", account);

        // Forward đến trang xem sửa tài khoản
        return "admin/managechangepassword";
	}

	@PostMapping("/managechangepassword")
	public ResponseEntity<?> manageChangePassword(@RequestParam("accountId") Integer accountId,
												  @RequestParam("currentPassword") String currentPassword,
												  @RequestParam("newPassword") String newPassword,
												  @RequestParam("confirmPassword") String confirmPassword,
												  HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Truy xuất dữ liệu từ nguồn dữ liệu
		Account account = accountService.getAccountById(accountId);

		// Kiểm tra mật khẩu hiện tại
		if (!BCrypt.checkpw(currentPassword, account.getPassword())) {
			// Trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Mật khẩu hiện tại không đúng. Vui lòng thử lại.", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới có giống mật khẩu hiện tại không
		if (BCrypt.checkpw(newPassword, account.getPassword())) {
			// Trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Vui lòng chọn mật khẩu khác mật khẩu cũ!", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới có phải là mật khẩu mạnh không
		if(!(newPassword.length() >= 8
				&& newPassword.matches(".*[A-Z].*")
				&& newPassword.matches(".*[a-z].*")
				&& newPassword.matches(".*\\d.*")
				&& newPassword.matches(".*\\W.*"))) {
			// Trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Mật khẩu không đủ mạnh! Mật khẩu mới phải có ít nhất 8 ký tự và"
					+ " chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt.", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới và mật khẩu nhập lại
		if (!newPassword.equals(confirmPassword)) {
			// Trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Mật khẩu nhập lại không khớp. Vui lòng thử lại.", HttpStatus.BAD_REQUEST);
		}

		// Băm mật khẩu mới sử dụng bcrypt
		String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		// Cập nhật mật khẩu mới cho tài khoản
		account.setPassword(hashedNewPassword);
		// Gọi phương thức updateAccount trong AccountService để cập nhật thông tin tài khoản
		accountService.updateAccount(account);
		// Trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Thay đổi mật khẩu thành công.", HttpStatus.OK);
	}

}
