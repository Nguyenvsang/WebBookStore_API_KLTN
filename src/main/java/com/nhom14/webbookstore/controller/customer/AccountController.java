package com.nhom14.webbookstore.controller.customer;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.service.AccountService;
import com.nhom14.webbookstore.service.CloudinaryService;

@RestController
@CrossOrigin
public class AccountController {
	private AccountService accountService;
	private CloudinaryService cloudinaryService;

	@Autowired
	public AccountController(AccountService accountService, CloudinaryService cloudinaryService) {
		super();
		this.accountService = accountService;
		this.cloudinaryService = cloudinaryService;
	}
	
	@GetMapping("/customer/registeraccount")
	public String registerAccountForm() {
		return "customer/registeraccount";
	}

	@PostMapping("/customer/registeraccount")
	public ResponseEntity<?> registerAccount(
			@RequestParam("username") String username,
			@RequestParam("password") String password,
			@RequestParam("address") String address,
			@RequestParam("phoneNumber") String phoneNumber,
			@RequestParam("email") String email,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName,
			@RequestParam("gender") String gender,
			@RequestParam("dob") String dob
	) {
		// Kiểm tra mật khẩu có phải là mật khẩu mạnh không
		if(!(password.length() >= 8
				&& password.matches(".*[A-Z].*")
				&& password.matches(".*[a-z].*")
				&& password.matches(".*\\d.*")
				&& password.matches(".*\\W.*"))) {
			// Hiển thị thông báo khi mật khẩu yếu
			return new ResponseEntity<>("Mật khẩu không đủ mạnh! Mật khẩu mới phải có ít nhất 8 ký tự và"
					+ " chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt.", HttpStatus.BAD_REQUEST);
		}
		// Băm mật khẩu sử dụng bcrypt
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

		Date dateOfBirth = Date.valueOf(dob);

		Account account = new Account(username, hashedPassword, address, phoneNumber, email, 1, 1);
		account.setFirstName(firstName);
		account.setLastName(lastName);
		account.setGender(gender);
		account.setDateOfBirth(dateOfBirth);
		account.setImg("");

		try {
			Account existingAccount = accountService.findAccountByUsername(username);
			if(existingAccount == null) {
				existingAccount = accountService.findAccountByPhoneNumber(phoneNumber);
			}
			if(existingAccount == null) {
				existingAccount = accountService.findAccountByEmail(email);
			}

			if (existingAccount != null) {
				// Username already exists
				return new ResponseEntity<>("Tên tài khoản, số điện thoại hoặc email đã tồn tại. Vui lòng tạo lại.", HttpStatus.BAD_REQUEST);
			} else {
				accountService.addAccount(account);
				return new ResponseEntity<>(account, HttpStatus.CREATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Đã xảy ra lỗi. Vui lòng thử lại sau.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/customer/loginaccount")
	public String loginAccountForm(Model model) {
		// tạo đối tượng account cho form dữ liệu
		Account account = new Account();
		model.addAttribute("account", account);
		return "customer/loginaccount";
	}

	@PostMapping("/customer/loginaccount")
	public ResponseEntity<?> loginAccount(@RequestParam("username") String username,
										  @RequestParam("password") String password,
										  HttpSession session,
										  HttpServletRequest request) {
		session.invalidate();
		session = request.getSession(true);
		// Kiểm tra đăng nhập bằng phương thức checkLogin
		boolean isValid = accountService.checkLogin(username, password);

		if (isValid) {
			// Nếu đăng nhập thành công, lưu thông tin tài khoản vào session
			Account account = accountService.findAccountByUsername(username);
			session.setAttribute("account", account);
			// Nếu đăng nhập thành công, trả về Account và mã trạng thái OK
			return new ResponseEntity<>(account, HttpStatus.OK);
		} else {
			// Nếu đăng nhập thất bại, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Sai tên đăng nhập hoặc mật khẩu", HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping("/customer/logoutaccount")
	public String showLogout() {
		return "customer/logoutaccount";
	}
	
	@PostMapping("/customer/logoutaccount")
	public ResponseEntity<?> logoutAccount(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
		// Trả về mã trạng thái OK
		return new ResponseEntity<>("Đã đăng xuất", HttpStatus.OK);
	}

	@GetMapping("/viewaccount")
	public ResponseEntity<?> viewAccount(HttpSession session) {
		Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

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
	
	@GetMapping("/updateaccount")
	public String showUpdateAccountForm(Model model, HttpSession session) {
		Account account = (Account) session.getAttribute("account");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (account == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/customer/loginaccount";
	    }
	    
	    // Lưu thông tin tài khoản vào model
	    model.addAttribute("account", account);

	    // Forward đến trang xem thông tin tài khoản
	    return "customer/updateaccount";
	}

	@PostMapping("/updateaccount")
	public ResponseEntity<?> updateAccount(
			@ModelAttribute("account") Account accountParam,
			@RequestParam("image") MultipartFile image,
			@RequestParam("dob") String dob,
			HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (account == null) {
            // Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
        }

        Account updateAccount = account;

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
            accountService.updateAccount(updateAccount);

            // Lưu thông tin tài khoản mới vào session
            session.setAttribute("account", updateAccount);

            // Tạo một Map để chứa thông báo và tài khoản
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đã cập nhật thành công!");
            response.put("account", updateAccount);

            // Trả về Map và mã trạng thái OK
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi khi cập nhật tài khoản.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	

	@GetMapping("/changepassword")
	public String showChangePasswordForm(HttpSession session) {
		Account account = (Account) session.getAttribute("account");

	    // Kiểm tra xem người dùng đã đăng nhập hay chưa
	    if (account == null) {
	        // Nếu chưa đăng nhập, chuyển hướng về trang đăng nhập
	        return "redirect:/customer/loginaccount";
	    }
	    
	    return "customer/changepassword";
	}

	@PostMapping("/changepassword")
	public ResponseEntity<?> changePassword(HttpSession session,
											@RequestParam("currentPassword") String currentPassword,
											@RequestParam("newPassword") String newPassword,
											@RequestParam("confirmPassword") String confirmPassword) {
		Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra mật khẩu hiện tại
		if (!BCrypt.checkpw(currentPassword, account.getPassword())) {
			// Hiển thị thông báo mật khẩu hiện tại không đúng
			return new ResponseEntity<>("Mật khẩu hiện tại không đúng. Vui lòng thử lại.", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới có giống mật khẩu hiện tại không
		if (BCrypt.checkpw(newPassword, account.getPassword())) {
			// Hiển thị thông báo giống mật khẩu cũ
			return new ResponseEntity<>("Vui lòng chọn mật khẩu khác mật khẩu cũ!", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới có phải là mật khẩu mạnh không
		if(!(newPassword.length() >= 8
				&& newPassword.matches(".*[A-Z].*")
				&& newPassword.matches(".*[a-z].*")
				&& newPassword.matches(".*\\d.*")
				&& newPassword.matches(".*\\W.*"))) {
			// Hiển thị thông báo khi mật khẩu yếu
			return new ResponseEntity<>("Mật khẩu không đủ mạnh! Mật khẩu mới phải có ít nhất 8 ký tự và"
					+ " chứa ít nhất một chữ cái viết hoa, một chữ cái viết thường, một số và một ký tự đặc biệt.", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra mật khẩu mới có giống mật khẩu nhập lại không
		if (!newPassword.equals(confirmPassword)) {
			// Hiển thị thông báo mật khẩu nhập lại không khớp
			return new ResponseEntity<>("Mật khẩu nhập lại không khớp. Vui lòng thử lại.", HttpStatus.BAD_REQUEST);
		}

		// Băm mật khẩu mới sử dụng bcrypt
		String hashedNewPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		// Cập nhật mật khẩu mới cho tài khoản
		account.setPassword(hashedNewPassword);
		// Gọi phương thức updateAccount trong AccountService để cập nhật thông tin tài khoản
		accountService.updateAccount(account);
		// Hiển thị thông báo thành công
		return new ResponseEntity<>("Thay đổi mật khẩu thành công.", HttpStatus.OK);
	}


}
