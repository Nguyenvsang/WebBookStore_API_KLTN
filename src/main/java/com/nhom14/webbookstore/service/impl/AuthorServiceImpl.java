package com.nhom14.webbookstore.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Author;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.repository.AuthorRepository;
import com.nhom14.webbookstore.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService {

	private AuthorRepository authorRepository;

	@Autowired
	public AuthorServiceImpl(AuthorRepository authorRepository) {
		super();
		this.authorRepository = authorRepository;
	}

	@Override
	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	@Override
	public Author getAuthorById(int id) {
		Optional<Author> author = authorRepository.findById(id);
	    return author.orElse(null);
	}

	@Override
	public List<Author> searchAuthorsByKeyword(List<Author> authors, String searchKeyword) {
		List<Author> result = new ArrayList<>();
	    String lowercaseKeyword = searchKeyword.toLowerCase().trim();
	    
	    for (Author author : authors) {
	        if (containsIgnoreCase(Integer.toString(author.getId()), lowercaseKeyword)
	                || containsIgnoreCase(author.getName(), lowercaseKeyword)
	                || containsIgnoreCase(author.getBio(), lowercaseKeyword)) {
	            result.add(author);
	        }
	    }

	    return result;
	}
	
	// Kiểm tra xem một chuỗi có chứa một chuỗi con cụ thể hay không,
	// mà không phân biệt chữ hoa chữ thường trong quá trình so sánh
	private boolean containsIgnoreCase(String text, String keyword) {
		return text.toLowerCase().contains(keyword);
	}

	@Override
	public void addAuthor(Author author) {
		authorRepository.save(author);
	}

	@Override
	public void updateAuthor(Author author) {
		authorRepository.save(author);
		
	}

	@Override
	public Author findAuthorByName(String authorName) {
		return authorRepository.findByName(authorName);
	}


}
