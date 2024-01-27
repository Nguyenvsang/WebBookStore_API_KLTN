package com.nhom14.webbookstore.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

	List<Book> findByStatus(int status);

	List<Book> findByCategoryAndStatus(Category category, int status);

	Book findByIdAndStatus(int id, int status);

	List<Book> findByCategory(Category category);

	Book findFirstByOrderByIdDesc();

	Book findByName(String name);
}
