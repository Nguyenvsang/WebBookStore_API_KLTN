package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.entity.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	List<OrderItem> findByOrder(Order order);
}
