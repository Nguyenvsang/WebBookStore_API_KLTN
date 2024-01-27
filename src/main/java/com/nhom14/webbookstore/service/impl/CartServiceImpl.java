package com.nhom14.webbookstore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Account;
import com.nhom14.webbookstore.entity.Cart;
import com.nhom14.webbookstore.repository.CartRepository;
import com.nhom14.webbookstore.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	private CartRepository cartRepository;

	@Autowired
	public CartServiceImpl(CartRepository cartRepository) {
		super();
		this.cartRepository = cartRepository;
	}
	
	public Cart getCartByAccount(Account account) {
        return cartRepository.findByAccount(account);
    }

	@Override
	public void addCart(Cart cart) {
		cartRepository.save(cart);
	}
}
