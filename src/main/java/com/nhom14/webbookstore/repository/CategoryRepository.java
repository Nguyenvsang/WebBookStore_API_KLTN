package com.nhom14.webbookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhom14.webbookstore.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByStatus(int status);

    Category findByName(String name);
}
