package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

	Order findFirstByAccountOrderByIdDesc(Account account);

	List<Order> findByAccount(Account account);
	
	List<Order> findByStatus(int status);

}
