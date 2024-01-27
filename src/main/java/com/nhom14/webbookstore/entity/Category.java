package com.nhom14.webbookstore.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;
	@Column(name = "status", columnDefinition = "INT NOT NULL")
    private int status; //hoạt động:1, ngừng kd:0
	//bi-directional one-to-many association with Book (de quen)
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Book> books;

    public Category() {
    }

    public Category(String name, int status) {
        this.name = name;
        this.status = status;
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

    public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	
}