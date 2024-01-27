package com.nhom14.webbookstore.controller.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Cart;
import com.nhom14.webbookstore.entity.CartItem;
import com.nhom14.webbookstore.entity.Order;
import com.nhom14.webbookstore.entity.OrderItem;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.CartItemService;
import com.nhom14.webbookstore.service.CartService;
import com.nhom14.webbookstore.service.OrderItemService;
import com.nhom14.webbookstore.service.OrderService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
public class OrderController {

	private OrderService orderService;
	private OrderItemService orderItemService;
	private CartService cartService;
	private CartItemService cartItemService;
	private BookService bookService;

	@Autowired
	public OrderController(OrderService orderService, 
			OrderItemService orderItemService, 
			CartService cartService, 
			CartItemService cartItemService,
			BookService bookService) {
		super();
		this.orderService = orderService;
		this.orderItemService = orderItemService;
		this.cartService = cartService;
		this.cartItemService = cartItemService;
		this.bookService = bookService;
		
	}

    @GetMapping("/shippinginformation")
    public ResponseEntity<?> shippingInformation(@RequestParam(value = "totalAmount", required = false) Double totalAmount, HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (account == null) {
            // Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
        }

        // Kiểm tra xem giỏ hàng có hàng không
        Cart cart = cartService.getCartByAccount(account);
        List<CartItem> cartItems = cartItemService.getCartItemsByCart(cart);
        if (cartItems.isEmpty() || totalAmount == null) {
            // Nếu giỏ hàng trống hoặc tổng số tiền là null, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
            return new ResponseEntity<>("Giỏ hàng trống hoặc tổng số tiền không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        // Tạo một Map để chứa thông tin tài khoản và tổng số tiền
        Map<String, Object> response = new HashMap<>();
        response.put("account", account);
        response.put("totalAmount", totalAmount);

        // Trả về Map và mã trạng thái OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/placeorder")
    public ResponseEntity<?> placeOrder(@RequestParam("name") String name,
                                        @RequestParam("address") String address,
                                        @RequestParam("phoneNumber") String phoneNumber,
                                        @RequestParam("email") String email,
                                        HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (account == null) {
            // Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
        }

        // Lấy giỏ hàng dựa trên tài khoản người dùng
        Cart cart = cartService.getCartByAccount(account);
        List<CartItem> cartItems = cartItemService.getCartItemsByCart(cart);
        if (cartItems.isEmpty()) {
            // Nếu giỏ hàng trống, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
            return new ResponseEntity<>("Giỏ hàng trống", HttpStatus.BAD_REQUEST);
        }

        // Tính toán tổng số tiền trong giỏ hàng
        double totalAmount = calculateTotalAmount(cartItems);

        // Tạo đối tượng đơn hàng
        Order order = new Order();
        order.setDateOrder(new Date());
        order.setTotalPrice(totalAmount);
        order.setName(name);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setEmail(email);
        order.setAccount(cart.getAccount());
        order.setStatus(0);

        // Thêm đơn hàng vào cơ sở dữ liệu
        orderService.addOrder(order);

        // Lấy ID của đơn hàng vừa thêm
        Order lastOrder = orderService.getLastOrder(cart.getAccount());

        // Tạo danh sách các mục đơn hàng (OrderItem) từ giỏ hàng
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            Book book = cartItem.getBook();
            double price = cartItem.getQuantity() * book.getSellPrice();
            orderItem.setPrice(price);
            orderItem.setBook(book);
            orderItem.setOrder(lastOrder);

            // Thêm mục đơn hàng vào cơ sở dữ liệu
            orderItemService.addOrderItem(orderItem);

            // Cập nhật số lượng sách và kiểm tra trạng thái
            int remainingQuantity = book.getQuantity() - cartItem.getQuantity();
            if (remainingQuantity <= 0) {
                book.setQuantity(0);
                book.setStatus(0);
            } else {
                book.setQuantity(remainingQuantity);
            }
            bookService.updateBook(book);

            // Xóa mục giỏ hàng khỏi giỏ hàng
            cartItemService.deleteCartItem(cartItem);
        }

        // Trả về thông báo thành công và mã trạng thái OK
        return new ResponseEntity<>("Đơn hàng đã được xác nhận", HttpStatus.OK);
    }


    private double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            totalAmount += cartItem.getQuantity() * cartItem.getBook().getSellPrice();
        }
        return totalAmount;
    }

    @GetMapping("/vieworders")
    public ResponseEntity<?> viewOrders(HttpSession session) {
        Account account = (Account) session.getAttribute("account");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (account == null) {
            // Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
            return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
        }

        // Lấy danh sách đơn hàng theo tài khoản
        List<Order> orders = orderService.getOrdersByAccount(account);

        // Kiểm tra xem có đơn hàng nào không
        if (orders.isEmpty()) {
            // Nếu không có đơn hàng nào, trả về thông báo và mã trạng thái OK
            return new ResponseEntity<>("Chưa có đơn hàng nào!", HttpStatus.OK);
        }

        // Trả về danh sách đơn hàng và mã trạng thái OK
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    @GetMapping("/orderconfirmation")
    public ResponseEntity<?> orderConfirmation(@RequestParam("orderId") Integer orderId) {
        // Lấy đơn hàng từ cơ sở dữ liệu dựa trên id
        Order order = orderService.getOrderById(orderId);

        // Kiểm tra xem đơn hàng có tồn tại hay không
        if (order == null) {
            // Nếu không tìm thấy đơn hàng, trả về thông báo lỗi và mã trạng thái NOT_FOUND
            return new ResponseEntity<>("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND);
        }

        // Lấy danh sách các mục đơn hàng (OrderItem) từ đơn hàng
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrder(order);

        // Tạo một Map để chứa thông tin đơn hàng và các mục đơn hàng
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("orderItems", orderItems);

        // Trả về Map và mã trạng thái OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
