package com.nhom14.webbookstore.service.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhom14.webbookstore.entity.Book;
import com.nhom14.webbookstore.entity.BookAuthor;
import com.nhom14.webbookstore.entity.Category;
import com.nhom14.webbookstore.repository.BookAuthorRepository;
import com.nhom14.webbookstore.repository.BookRepository;
import com.nhom14.webbookstore.repository.CategoryRepository;
import com.nhom14.webbookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;
	private CategoryRepository categoryRepository;
	private BookAuthorRepository bookAuthorRepository;
	
	@Autowired
	public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, BookAuthorRepository bookAuthorRepository) {
		super();
		this.bookRepository = bookRepository;
		this.categoryRepository = categoryRepository;
		this.bookAuthorRepository = bookAuthorRepository;
	}

	@Override
	public List<Book> getActiveBooks() {
		return bookRepository.findByStatus(1);
	}

	@Override
	public List<Book> getActiveBooksByCategory(int categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
	    if (category != null) {
	        return bookRepository.findByCategoryAndStatus(category, 1);
	    }
	    return Collections.emptyList();
	}

	@Override
	public List<Book> searchBooksByKeyword(List<Book> books, String searchKeyword) {
		List<Book> result = new ArrayList<>();
	    Set<Integer> addedBookIds = new HashSet<>();
	    String lowercaseKeyword = searchKeyword.toLowerCase().trim();

	    for (Book book : books) {
	        List<BookAuthor> bookAuthors = bookAuthorRepository.findByBook(book);
	        boolean isAuthorMatched = false;

	        for (BookAuthor bookAuthor : bookAuthors) {
	            String authorName = bookAuthor.getAuthor().getName();
	            if (containsIgnoreCase(authorName, searchKeyword)) {
	                isAuthorMatched = true;
	                break;
	            }
	        }

	        if (isAuthorMatched
	        		|| containsIgnoreCase(Integer.toString(book.getId()), lowercaseKeyword)
	                || containsIgnoreCase(book.getName(), lowercaseKeyword)
	                || containsIgnoreCase(book.getDescription(), lowercaseKeyword)
	                || containsIgnoreCase(book.getDetail(), lowercaseKeyword)
	                || containsIgnoreCase(book.getPublisher(), lowercaseKeyword)
	                || containsIgnoreCase(Double.toString(book.getSellPrice()), lowercaseKeyword)
	                || (book.getCategory() != null && containsIgnoreCase(book.getCategory().getName(), lowercaseKeyword))) {
	            if (!addedBookIds.contains(book.getId())) {
	                result.add(book);
	                addedBookIds.add(book.getId());
	            }
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
	public Book getActiveBookById(int id) {
		try {
	        return bookRepository.findByIdAndStatus(id, 1);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null; // Trả về null nếu không tìm thấy cuốn sách
	}

	@Override
	public Book getBookById(int id) {
		Optional<Book> book = bookRepository.findById(id);
	    return book.orElse(null);
	}

	@Override
	public void updateBook(Book book) {
		bookRepository.save(book);
		
	}

	@Override
	public List<Book> getAllBooks() {
		return bookRepository.findAll(); // Nếu không có sách sẽ trả về empty list
	}

	@Override
	public List<Book> getBooksByCategory(Integer categoryId) {
		Category category = categoryRepository.findById(categoryId).orElse(null);
	    if (category != null) {
	        return bookRepository.findByCategory(category);
	    }
	    return Collections.emptyList();
	}

	@Override
	public void addBook(Book newBook) {
		bookRepository.save(newBook);
	}

	@Override
	public Book getLastBook() {
		return bookRepository.findFirstByOrderByIdDesc();// trả null nếu không tìm thấy
	}

	@Override
	public List<Book> filterBooksByPriceRange(List<Book> books, Double priceMin, Double priceMax) {
	    List<Book> result = new ArrayList<>();

	    for (Book book : books) {
	        if (book.getSellPrice() >= priceMin && book.getSellPrice() <= priceMax) {
	            result.add(book);
	        }
	    }

	    return result;
	}

	@Override
	public List<Book> sortBooksByPriceAscending(List<Book> books) {
		List<Book> sortedBooks = new ArrayList<>(books);
	    sortedBooks.sort(Comparator.comparingDouble(Book::getSellPrice));
	    return sortedBooks;
	}

	@Override
	public List<Book> sortBooksByPriceDescending(List<Book> books) {
		List<Book> sortedBooks = new ArrayList<>(books);
	    sortedBooks.sort(Comparator.comparingDouble(Book::getSellPrice).reversed());
	    return sortedBooks;
	}

	@Override
	public List<Book> sortBooksByNameAscending(List<Book> books) {
		List<Book> sortedBooks = new ArrayList<>(books);
	    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
	    sortedBooks.sort(Comparator.comparing(Book::getName, collator));
	    return sortedBooks;
	}

	@Override
	public List<Book> sortBooksByNameDescending(List<Book> books) {
		List<Book> sortedBooks = new ArrayList<>(books);
	    Collator collator = Collator.getInstance(new Locale("vi", "VN"));
	    sortedBooks.sort(Comparator.comparing(Book::getName, collator).reversed());
	    return sortedBooks;
	}

	@Override
	public List<Book> filterBooksByPublisher(List<Book> books, String string) {
		List<Book> filteredBooks = new ArrayList<>();
		String lowercaseKeyword = string.toLowerCase().trim();
	    for (Book book : books) {
	        if (containsIgnoreCase(book.getPublisher().trim(), lowercaseKeyword)) {
	            filteredBooks.add(book);
	        }
	    }
	    return filteredBooks;
	}

	@Override
	public Book getBookByName(String name) {
		return bookRepository.findByName(name);
	}

	@Override
	public List<Book> getBooksByStatus(int status) {
		return bookRepository.findByStatus(status);
	}

}
