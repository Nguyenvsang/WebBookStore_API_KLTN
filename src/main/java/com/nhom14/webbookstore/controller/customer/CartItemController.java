package com.nhom14.webbookstore.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.CartItem;
import com.nhom14.webbookstore.service.CartItemService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CartItemController {

	private CartItemService cartItemService;

	@Autowired
	public CartItemController(CartItemService cartItemService) {
		super();
		this.cartItemService = cartItemService;
	}
	
	@PostMapping("/updatecartitem")
	public ResponseEntity<?> updateCartItem(@RequestParam("itemId") int itemId,
	        @RequestParam("quantity") int quantity,
	        HttpSession session) {
	    
	    Account account = (Account) session.getAttribute("account");

		// Kiểm tra xem người dùng đã đăng nhập hay chưa
		if (account == null) {
			// Nếu chưa đăng nhập, trả về thông báo lỗi và mã trạng thái UNAUTHORIZED
			return new ResponseEntity<>("Chưa đăng nhập", HttpStatus.UNAUTHORIZED);
		}
	    
	    CartItem cartItem = cartItemService.getCartItemById(itemId);
	    if (cartItem == null) {
	        // Không tìm thấy mặt hàng trong giỏ hàng
			return new ResponseEntity<>("Không tìm thấy mặt hàng!", HttpStatus.NOT_FOUND);
	    }
	    
	    cartItem.setQuantity(quantity);
	    cartItemService.updateCartItem(cartItem);
		return new ResponseEntity<>("Đã cập nhật mặt hàng thành công", HttpStatus.OK);
	}
}
