package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.entity.OrderItem;

public interface OrderItemService {

	// Phương thức để thêm một OrderItem mới
	void addOrderItem(OrderItem orderItem);

	// Phương thức lấy OrderItems theo đơn hàng
	List<OrderItem> getOrderItemsByOrder(Order order);

}
