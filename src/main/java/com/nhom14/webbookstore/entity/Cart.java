package com.nhom14.webbookstore.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	//bi-directional one-to-one association with Account
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private Account account;

	//bi-directional one-to-many association with Cart Item
	@OneToMany(fetch = FetchType.EAGER, mappedBy="cart")
	@JsonIgnore
	private List<CartItem> cartitems;
	
	public Cart() {
		
	}

	public Cart(Account account, List<CartItem> cartitems) {
		super();
		this.account = account;
		this.cartitems = cartitems;
	}

	public Cart(Account account) {
		super();
		this.account = account;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public List<CartItem> getCartitems() {
		return cartitems;
	}

	public void setCartitems(List<CartItem> cartitems) {
		this.cartitems = cartitems;
	}
	
	
}