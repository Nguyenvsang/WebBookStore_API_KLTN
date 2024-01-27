package com.nhom14.webbookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookImage;
import com.nhom14.webbookstore.repository.BookImageRepository;
import com.nhom14.webbookstore.service.BookImageService;

@Service
public class BookImageServiceImpl implements BookImageService {

	private BookImageRepository bookImageRepository;

	@Autowired
	public BookImageServiceImpl(BookImageRepository bookImageRepository) {
		super();
		this.bookImageRepository = bookImageRepository;
	}

	@Override
	public List<BookImage> getByBook(Book book) {
		return bookImageRepository.findByBook(book);
	}

	@Override
	public void updateBookImage(BookImage bookImage) {
		bookImageRepository.save(bookImage);
		
	}

	@Override
	public void addBookImage(BookImage bookImage) {
		bookImageRepository.save(bookImage);
	}
	
	
}
