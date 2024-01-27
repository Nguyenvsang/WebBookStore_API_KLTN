package com.nhom14.webbookstore.entity;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "first_name", columnDefinition = "varchar(255)")
	private String firstName;
	@Column(name = "last_name", columnDefinition = "varchar(255)")
	private String lastName;
	@Column(name = "username", columnDefinition = "varchar(255) NOT NULL")
	private String username;
	@Column(name = "password", columnDefinition = "varchar(255) NOT NULL")
	private String password;
	@Column(name = "gender", columnDefinition = "varchar(255)")
	private String gender;
	@Column(name = "img", columnDefinition = "char(255)")
	private String img;
	@Column(name = "date_of_birth", columnDefinition = "DATE")
	private Date dateOfBirth;
	@Column(name = "address", columnDefinition = "VARCHAR(255) NOT NULL")
	private String address;
	@Column(name = "phone_number", columnDefinition = "varchar(255) NOT NULL")
	private String phoneNumber;
	@Column(name = "email", columnDefinition = "char(255) NOT NULL")
	private String email;
	@Column(name = "account_type", columnDefinition = "INT NOT NULL")
	private int accountType; //customer:1, admin:0
	@Column(name = "status", columnDefinition = "INT NOT NULL")
	private int status; //hoạt động:1, ngừng hd:0

	@OneToOne(fetch = FetchType.EAGER, mappedBy = "account")
	@JsonIgnore
	private Cart cart;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "account")
	@JsonIgnore
	private List<Order> order;

	// Các phương thức getter, setter, constructor, toString...

	public Account() {
	    // default constructor
	}

	public Account(String username, String password, String address, String phoneNumber, String email, int accountType, int status) {
		super();
		this.username = username;
		this.password = password;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.accountType = accountType;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public int getAccountType() {
		return accountType;
	}

	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}
	
}
