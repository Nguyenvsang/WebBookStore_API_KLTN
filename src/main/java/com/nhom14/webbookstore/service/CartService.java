package com.nhom14.webbookstore.service;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Cart;

public interface CartService {

	// Lấy giỏ hàng theo người dùng
	Cart getCartByAccount(Account account);
	
	// Phương thức để thêm một giỏ hàng mới
	void addCart(Cart cart);

}
