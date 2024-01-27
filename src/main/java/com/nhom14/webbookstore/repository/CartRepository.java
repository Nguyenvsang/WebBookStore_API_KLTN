package com.nhom14.webbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByAccount(Account account);

}
