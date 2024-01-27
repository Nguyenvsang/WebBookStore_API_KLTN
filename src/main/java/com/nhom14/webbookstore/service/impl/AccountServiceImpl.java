package com.nhom14.webbookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.repository.AccountRepository;
import com.nhom14.webbookstore.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;
	
	@Autowired
	public AccountServiceImpl(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	@Override
	public boolean checkLogin(String username, String password) {
		Account account = accountRepository.findByUsernameAndAccountTypeAndStatus(username, 1, 1); // customer: 1, active: 1
	    if (account != null) {
	        String hashedPassword = account.getPassword();
	        return BCrypt.checkpw(password, hashedPassword);
	    }
	    return false;
	}

	@Override
	public Account findAccountByUsername(String username) {
		return accountRepository.findByUsername(username);
	}
	
	@Override
    public void addAccount(Account account) {
        accountRepository.save(account);
    }

	@Override
	public void updateAccount(Account account) {
		accountRepository.save(account);
	}

	@Override
	public boolean checkLoginAdmin(String username, String password) {
		Account admin = accountRepository.findByUsernameAndAccountTypeAndStatus(username, 0, 1); // admin: 0, active: 1
	    if (admin != null) {
	        String hashedPassword = admin.getPassword();
	        return BCrypt.checkpw(password, hashedPassword);
	    }
	    return false;
	}

	@Override
	public List<Account> getAllAccounts() {
		return accountRepository.findAll(); // Nếu không có order sẽ trả về empty list
	}

	@Override
	public List<Account> getAccountsByStatus(int status) {
		return accountRepository.findByStatus(status);
	}

	@Override
	public List<Account> searchAccountsByKeyword(List<Account> accounts, String searchKeyword) {
		List<Account> result = new ArrayList<>();
	    String lowercaseKeyword = searchKeyword.toLowerCase().trim();
	    
	    for (Account account : accounts) {
	    	if (containsIgnoreCase(Integer.toString(account.getId()), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getFirstName(), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getLastName(), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getUsername(), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getPassword(), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getGender(), lowercaseKeyword)
	    			|| containsIgnoreCase(account.getImg(), lowercaseKeyword)
	                || containsIgnoreCase(account.getDateOfBirth().toString(), lowercaseKeyword)
	                || containsIgnoreCase(account.getAddress(), lowercaseKeyword)
	                || containsIgnoreCase(account.getPhoneNumber(), lowercaseKeyword)
	                || containsIgnoreCase(account.getEmail(), lowercaseKeyword)
	                || containsIgnoreCase(Integer.toString(account.getAccountType()), lowercaseKeyword)
	                || containsIgnoreCase(Integer.toString(account.getStatus()), lowercaseKeyword)) {
	            result.add(account);
	        }
	    }
	    
	    return result;
	}
	
	// Kiểm tra xem một chuỗi có chứa một chuỗi con cụ thể hay không,
	// mà không phân biệt chữ hoa chữ thường trong quá trình so sánh
	private boolean containsIgnoreCase(String text, String keyword) {
		return text.toLowerCase().contains(keyword);
	}

	@Override
	public Account getAccountById(int accountId) {
		Optional<Account> account = accountRepository.findById(accountId);
	    return account.orElse(null);
	}

	@Override
	public Account findAccountByPhoneNumber(String phoneNumber) {
		Optional<Account> account = accountRepository.findByPhoneNumber(phoneNumber);
	    return account.orElse(null);
	}

	@Override
	public Account findAccountByEmail(String email) {
		Optional<Account> account = accountRepository.findByEmail(email);
	    return account.orElse(null);
	}

}
