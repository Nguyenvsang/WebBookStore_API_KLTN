package com.nhom14.webbookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.entity.OrderItem;
import com.nhom14.webbookstore.repository.OrderItemRepository;
import com.nhom14.webbookstore.service.OrderItemService;

@Service
public class OrderItemServiceImpl implements OrderItemService {

	private OrderItemRepository orderItemRepository;

	@Autowired
	public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
		super();
		this.orderItemRepository = orderItemRepository;
	}

	@Override
	public void addOrderItem(OrderItem orderItem) {
		orderItemRepository.save(orderItem);
		
	}

	@Override
	public List<OrderItem> getOrderItemsByOrder(Order order) {
		return orderItemRepository.findByOrder(order);
	}
	
	
}
