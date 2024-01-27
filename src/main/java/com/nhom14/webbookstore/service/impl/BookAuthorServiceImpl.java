package com.nhom14.webbookstore.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookAuthor;
import com.nhom14.webbookstore.repository.BookAuthorRepository;
import com.nhom14.webbookstore.service.BookAuthorService;

@Service
public class BookAuthorServiceImpl implements BookAuthorService {

	private BookAuthorRepository bookAuthorRepository;
	
	@Autowired
	public BookAuthorServiceImpl(BookAuthorRepository bookAuthorRepository) {
		super();
		this.bookAuthorRepository = bookAuthorRepository;
	}


	@Override
	public List<BookAuthor> getByBook(Book book) {
		return bookAuthorRepository.findByBook(book);
	}


	@Override
	public void deleteBookAuthorsByBook(Book book) {
		List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook(book);
	    bookAuthorRepository.deleteAll(bookAuthors);
	}


	@Override
	public void addBookAuthor(BookAuthor bookAuthor) {
		bookAuthorRepository.save(bookAuthor);
	}

}
