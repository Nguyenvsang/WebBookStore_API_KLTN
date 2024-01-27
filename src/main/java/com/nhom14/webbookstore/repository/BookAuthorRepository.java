package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookAuthor;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Integer> {

	List<BookAuthor> findByBook(Book book);

}
