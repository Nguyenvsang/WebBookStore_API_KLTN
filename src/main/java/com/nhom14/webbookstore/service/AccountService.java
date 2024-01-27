package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Account;

public interface AccountService {
	// Phương thức để kiểm tra đăng nhập của một tài khoản
	boolean checkLogin(String username, String password);
	
	// Phương thức để tìm kiếm một tài khoản theo username
	Account findAccountByUsername(String username);
	
	// Phương thức để thêm một tài khoản mới
    void addAccount(Account account);

    // Phương thức để cập nhật thông tin của một tài khoản
	void updateAccount(Account account);

	// Phương thức để kiểm tra đăng nhập của một tài khoản admin 
	boolean checkLoginAdmin(String username, String password);

	// Phương thức để lấy danh sách tất cả các tài khoản
	List<Account> getAllAccounts();

	// Phương thức để lấy danh sách tài khoản theo trạng thái
	List<Account> getAccountsByStatus(int status);

	// Phương thức tìm kiếm tài khoản dựa trên từ khóa
	List<Account> searchAccountsByKeyword(List<Account> accounts, String searchKeyword);

	// Phương thức để tìm kiếm một tài khoản theo id
	Account getAccountById(int accountId);

	// Phương thức để tìm tài khoản theo số điện thoại
	Account findAccountByPhoneNumber(String phoneNumber);

	// Phương thức để tìm tài khoản theo email
	Account findAccountByEmail(String email);
}
