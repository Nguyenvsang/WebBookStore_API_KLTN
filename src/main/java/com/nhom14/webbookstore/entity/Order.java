package com.nhom14.webbookstore.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`order`")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "date_order", columnDefinition = "date NOT NULL")
	private Date dateOrder; 
	@Column(name = "total_price", columnDefinition = "double NOT NULL")
	private double totalPrice;
	
	// Tuy la mot nguoi dung nhung ho co quyen dung nhieu email, sdt 
	@Column(name = "name", columnDefinition = "varchar(255) NOT NULL")
	private String name;
	@Column(name = "address", columnDefinition = "varchar(255) NOT NULL")
	private String address;
	@Column(name = "phone_number", columnDefinition = "varchar(20) NOT NULL")
    private String phoneNumber;
	@Column(name = "email", columnDefinition = "char(255) NOT NULL")
    private String email;
	//bi-directional many-to-one association with Account
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    @Column(name = "status", columnDefinition = "int NOT NULL")
    private int status; // 0: Chờ xác nhận, 1: Chờ lấy hàng, 
    					// 2: Đang giao, 3: Đã giao
    
    //bi-directional one-to-many association with OrderItem
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
	@JsonIgnore
    private List<OrderItem> orderitems;

	public Order() {
		
	}

	public Order(Date dateOrder, double totalPrice, String name, String address, String phoneNumber, String email,
			Account account, int status, List<OrderItem> orderitems) {
		super();
		this.dateOrder = dateOrder;
		this.totalPrice = totalPrice;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.account = account;
		this.status = status;
		this.orderitems = orderitems;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateOrder() {
		return dateOrder;
	}

	public void setDateOrder(Date dateOrder) {
		this.dateOrder = dateOrder;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<OrderItem> getOrderitems() {
		return orderitems;
	}

	public void setOrderitems(List<OrderItem> orderitems) {
		this.orderitems = orderitems;
	}

	
}