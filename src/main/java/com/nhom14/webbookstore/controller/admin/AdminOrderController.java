package com.nhom14.webbookstore.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.OrderService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class AdminOrderController {
	private OrderService orderService;
	private BookService bookService;
	
	@Autowired
	public AdminOrderController(OrderService orderService, BookService bookService) {
		super();
		this.orderService = orderService;
		this.bookService = bookService;
	}

	@GetMapping("/manageorders")
	public ResponseEntity<?> manageOrders(@RequestParam(value = "status", required = false) Integer status,
										  @RequestParam(value = "search", required = false) String searchKeyword,
										  @RequestParam(value = "page", required = false, defaultValue = "1") Integer currentPage,
										  HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		List<Order> orders;
		int totalOrders;

		// Số danh mục hiển thị trên mỗi trang
		int recordsPerPage = 10;
		int start;
		int end;
		int totalPages;

		if (status == null || (status == -1)) {
			orders = orderService.getAllOrders();
		} else {
			orders = orderService.getOrdersByStatus(status);
		}

		if (searchKeyword != null && !searchKeyword.isEmpty()) {
			orders = orderService.searchOrdersByKeyword(orders, searchKeyword);
		}

		// Lấy tổng số lượng đơn hàng
		totalOrders = orders.size();

		// Tính toán vị trí bắt đầu và kết thúc của đơn hàng trên trang hiện tại
		start = (currentPage - 1) * recordsPerPage;
		end = Math.min(start + recordsPerPage, totalOrders);

		// Lấy danh sách đơn hàng trên trang hiện tại
		List<Order> ordersOnPage = orders.subList(start, end);

		// Tính toán số trang
		totalPages = (int) Math.ceil((double) totalOrders / recordsPerPage);

		// Tổng số tất cả các đơn hàng
		int totalAllOrders = orderService.getAllOrders().size();

		// Tạo một đối tượng Map để chứa tất cả các thuộc tính cần trả về
		Map<String, Object> response = new HashMap<>();
		response.put("orders", ordersOnPage);
		response.put("totalOrders", totalOrders);
		response.put("totalPages", totalPages);
		response.put("currentPage", currentPage);
		response.put("totalAllOrders", totalAllOrders);

		// Trả về đối tượng Map dưới dạng JSON và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@PostMapping("/updateorderstatus")
	public ResponseEntity<?> updateOrderStatus(@RequestParam("orderId") int orderId,
											   @RequestParam("status") int status,
											   HttpSession session) {
		Account admin = (Account) session.getAttribute("admin");

		// Kiểm tra xem admin đã đăng nhập hay chưa
		if (admin == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Lấy đơn hàng từ Service
		Order order = orderService.getOrderById(orderId);

		// Cập nhật trạng thái đơn hàng
		order.setStatus(status);

		// Cập nhật đơn hàng thông qua Service
		orderService.updateOrder(order);

		// Trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Đã cập nhật trạng thái đơn hàng thành công", HttpStatus.OK);
	}

}
