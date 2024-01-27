package com.nhom14.webbookstore.service;

import java.util.List;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Cart;
import com.nhom14.webbookstore.entity.CartItem;

public interface CartItemService {

	//Lấy CartItem theo Cart và Book
	CartItem getCartItemByCartAndBook(Cart cart, Book book);

	//Thêm sản phẩm giỏ hàng mới 
	void addCartItem(CartItem cartItem);

	// Cập nhật lại sản phẩm giỏ hàng
	void updateCartItem(CartItem cartItem);

	//Lấy danh sách CartItem theo Cart 
	List<CartItem> getCartItemsByCart(Cart cart);

	// Lấy CartItem theo Id
	CartItem getCartItemById(int itemId);

	// Xóa một cartItem
	void deleteCartItem(CartItem cartItem);


}
