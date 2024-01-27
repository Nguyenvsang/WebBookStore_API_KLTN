package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Cart;
import com.nhom14.webbookstore.entity.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	CartItem findByCartAndBook(Cart cart, Book book);

	List<CartItem> findByCart(Cart cart);
}
