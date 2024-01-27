package com.nhom14.webbookstore.controller.customer;

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

import jakarta.servlet.http.HttpSession;
import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Cart;
import com.nhom14.webbookstore.entity.CartItem;
import com.nhom14.webbookstore.service.BookService;
import com.nhom14.webbookstore.service.CartItemService;
import com.nhom14.webbookstore.service.CartService;

@RestController
@CrossOrigin
public class CartController {

	private CartService cartService;
	private BookService bookService;
	private CartItemService cartItemService;

	@Autowired
	public CartController(CartService cartService, BookService bookService, CartItemService cartItemService) {
		super();
		this.cartService = cartService;
		this.bookService = bookService;
		this.cartItemService = cartItemService;
	}

	@PostMapping("/addtocart")
	public ResponseEntity<?> addToCart(@RequestParam("bookId") int bookId,
									   @RequestParam("quantity") int quantity,
									   HttpSession session) {
		Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Kiểm tra số lượng hợp lệ
		if (quantity <= 0) {
			// Số lượng không hợp lệ, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
			return new ResponseEntity<>("Số lượng không hợp lệ", HttpStatus.BAD_REQUEST);
		}

		// Kiểm tra xem giỏ hàng của người dùng đã tồn tại hay chưa
		Cart cart = cartService.getCartByAccount(account);
		if (cart == null) {
			// Nếu giỏ hàng chưa tồn tại, thêm giỏ hàng mới
			cart = new Cart(account);
			cartService.addCart(cart);
		}

		// Tìm CartItem theo cart và book
		Book book = bookService.getActiveBookById(bookId);
		CartItem cartItem = cartItemService.getCartItemByCartAndBook(cart, book);

		if (cartItem == null) {
			// Nếu CartItem chưa tồn tại, tạo mới và thêm vào giỏ hàng
			cartItem = new CartItem(quantity, cart, book);
			cartItemService.addCartItem(cartItem);
		} else {
			// Nếu CartItem đã tồn tại, cộng dồn số lượng
			int currentQuantity = cartItem.getQuantity();
			int newQuantity = currentQuantity + quantity;

			// Kiểm tra số lượng tồn kho
			if (newQuantity > book.getQuantity()) {
				// Số lượng vượt quá số lượng tồn kho, trả về thông báo lỗi và mã trạng thái BAD_REQUEST
				return new ResponseEntity<>("Số lượng vượt quá số lượng tồn kho", HttpStatus.BAD_REQUEST);
			}

			cartItem.setQuantity(newQuantity);
			cartItemService.updateCartItem(cartItem);
		}

		// Thành công, trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Thêm vào giỏ hàng thành công", HttpStatus.OK);
	}


	@GetMapping("/viewcart")
	public ResponseEntity<?> viewCart(HttpSession session) {
		Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		Cart cart = cartService.getCartByAccount(account);
		if (cart == null) {
			// Nếu giỏ hàng chưa tồn tại, thêm giỏ hàng mới
			cart = new Cart(account);
			cartService.addCart(cart);
		}

		// Lấy danh sách các mục trong giỏ hàng
		List<CartItem> cartItems = cartItemService.getCartItemsByCart(cart);

		// Kiểm tra xem giỏ hàng có hàng không
		if (cartItems.isEmpty()) {
			// Trả về thông báo lỗi và mã trạng thái NOT_FOUND
			return new ResponseEntity<>("Giỏ hàng trống!", HttpStatus.NOT_FOUND);
		}

		// Tính toán tổng số tiền trong giỏ hàng
		double totalAmount = calculateTotalAmount(cartItems);

		// Tổng số đầu sách trong giỏ hàng
		int totalAllCartItems = cartItems.size();

		// Tạo một Map để chứa thông tin giỏ hàng và tổng số tiền
		Map<String, Object> response = new HashMap<>();
		response.put("cartItems", cartItems);
		response.put("totalAmount", totalAmount);
		response.put("totalAllCartItems", totalAllCartItems);

		// Trả về Map và mã trạng thái OK
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	private double calculateTotalAmount(List<CartItem> cartItems) {
        double totalAmount = 0.0;
        for (CartItem cartItem : cartItems) {
            totalAmount += cartItem.getQuantity() * cartItem.getBook().getSellPrice();
        }
        return totalAmount;
    }

	@DeleteMapping("/removefromcart")
	public ResponseEntity<?> removeFromCart(@RequestParam("itemId") int itemId, HttpSession session) {
		Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}

		// Xóa CartItem khỏi giỏ hàng
		CartItem cartItem = cartItemService.getCartItemById(itemId);
		cartItemService.deleteCartItem(cartItem);

		// Trả về thông báo thành công và mã trạng thái OK
		return new ResponseEntity<>("Đã xóa khỏi giỏ hàng", HttpStatus.OK);
	}

}
