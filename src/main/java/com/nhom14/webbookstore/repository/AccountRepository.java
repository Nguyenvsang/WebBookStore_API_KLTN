package com.nhom14.webbookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhom14.webbookstore.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findByUsernameAndAccountTypeAndStatus(String username, int accountType, int status);

	Account findByUsername(String username);

	List<Account> findByStatus(int status);
	
	Optional<Account> findByPhoneNumber(String phoneNumber);
	
    Optional<Account> findByEmail(String email);
}
