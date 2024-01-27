package com.nhom14.webbookstore.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "book")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name", columnDefinition = "varchar(255) NOT NULL")
	private String name;
	
	@Column(name = "cost_price", columnDefinition = "double NOT NULL")
	private double costPrice;
	@Column(name = "sell_price", columnDefinition = "double NOT NULL")
	private double sellPrice;

	//bi-directional many-to-one association with Category
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	private Category category;

	@Column(name = "publisher", columnDefinition = "varchar(255) NOT NULL")
	private String publisher;
	@Column(name = "description", columnDefinition = "text")
	private String description; // mô tả sơ lược
	@Column(name = "status", columnDefinition = "INT NOT NULL")
	private int status; //hoạt động:1, ngừng kd:0
	@Column(name = "detail", columnDefinition = "text")
	private String detail; // chi tiết nội dung 
	@Column(name = "quantity", columnDefinition = "INT NOT NULL")
	private int quantity;
	
	//bi-directional one-to-many association to BookImage
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
	@JsonIgnore
	private List<BookImage> bookImages;

	//bi-directional one-to-many association to OrderItem
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
	@JsonIgnore
	private List<OrderItem> orderItems;

	//bi-directional one-to-many association with CartItem
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
	@JsonIgnore
	private List<CartItem> cartItems;
	
	//bi-directional one-to-many association to BookAuthor
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
	@JsonIgnore
	private List<BookAuthor> bookAuthors;

    public Book() {
    }

	public Book(int id, String name, double costPrice, double sellPrice, Category category, String publisher,
			String description, int status, String detail, int quantity, List<BookImage> bookImages,
			List<OrderItem> orderItems, List<CartItem> cartItems, List<BookAuthor> bookAuthors) {
		super();
		this.id = id;
		this.name = name;
		this.costPrice = costPrice;
		this.sellPrice = sellPrice;
		this.category = category;
		this.publisher = publisher;
		this.description = description;
		this.status = status;
		this.detail = detail;
		this.quantity = quantity;
		this.bookImages = bookImages;
		this.orderItems = orderItems;
		this.cartItems = cartItems;
		this.bookAuthors = bookAuthors;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public List<BookImage> getBookImages() {
		return bookImages;
	}

	public void setBookImages(List<BookImage> bookImages) {
		this.bookImages = bookImages;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public List<BookAuthor> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(List<BookAuthor> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	
	
}