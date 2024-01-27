package com.nhom14.webbookstore.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.entity.OrderItem;
import com.nhom14.webbookstore.service.OrderItemService;
import com.nhom14.webbookstore.service.OrderService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AdminOrderItemController {

	private OrderItemService orderItemService;
	private OrderService orderService;
	
	public AdminOrderItemController(OrderItemService orderItemService, OrderService orderService) {
		super();
		this.orderItemService = orderItemService;
		this.orderService = orderService;
	}

	@GetMapping("/manageorderitems")
	public ResponseEntity<?> manageOrderItems(@RequestParam int orderId, HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Lấy đối tượng Order từ OrderService bằng orderId
		Order order = orderService.getOrderById(orderId);

		// Lấy danh sách OrderItem từ OrderItemService
		List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);

		// Tạo một đối tượng Map để chứa tất cả các thuộc tính cần trả về
		Map<String, Object> response = new HashMap<>();
		response.put("order", order);
		response.put("orderItems", orderItems);

		// Trả về đối tượng Map dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


}
