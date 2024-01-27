package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookImage;

public interface BookImageRepository extends JpaRepository<BookImage, Integer> {

	List<BookImage> findByBook(Book book);

}
