package com.nhom14.webbookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhom14.webbookstore.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    Author findByName(String name);
}
