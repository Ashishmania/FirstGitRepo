package com.example.demo.RestController;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.Entity.AdditionalInformation;
import com.example.demo.Entity.Author;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.BookCategory;
import com.example.demo.Service.AdminService;
import java.util.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	// ------------------------ BookCategory Management
	// -------------------------------------------------------

	// Adding Bookcategories
	@PostMapping("/categories")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<BookCategory> addBookCategory(@RequestBody BookCategory category) {
		String categoryName = category.getName();
		BookCategory bookCategory = adminService.addBookCategory(categoryName);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookCategory);
	}

	// Admin Can view list of Bookcategories
	@GetMapping("/categories")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<BookCategory>> getAllCategories() {
		List<BookCategory> categories = adminService.getAllCategories();
		return ResponseEntity.ok(categories);

	}

	// Update BookCategory
	

	// Delete BookCategoryByName
	@DeleteMapping("/categories/name/{categoryName}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteBookByCategoryName(@PathVariable String categoryName) {
		adminService.deleteBookCategoryByName(categoryName);
		return ResponseEntity.ok("Book Category deleted Successfully");
	}

	// ------------------------ Author Management
	// -------------------------------------------------------

	// Adding author-
	@PostMapping("/addAuthor")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<Author> saveAuthor(@RequestBody Author author) {
		String authorName = author.getName();
		Author savedAuthor = adminService.addAuthor(authorName);
		return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
	}

	// Assigning category to author by CategoryID - Updating Authors
	@PutMapping("/{authorId}/assign-categoryById/{categoryId}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<Author> assignCategoryToAuthorById(@PathVariable Long authorId,
			@PathVariable Long categoryID) {
		Author updatedAuthor = adminService.assignCategoryToAuthorById(authorId, categoryID);
		return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
	}

	// Assigning category to author by CategoryName
	@PutMapping("/{authorId}/assign-categoryByName/{categoryName}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<Author> assignCategoryToAuthorByName(@PathVariable Long authorId,
			@PathVariable String categoryName) {
		Author updatedAuthor = adminService.assignCategoryToAuthorByName(authorId, categoryName);
		return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);

	}

	// getAuthors
	@GetMapping("/authors")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<Author>> getAuthors(){
		List<Author> authorList=adminService.getAuthors();
		return ResponseEntity.ok(authorList);	
	}

	// getAuthorByCategory**
	@GetMapping("/authors/category/{}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<Author>> getAuthorsByCategoryName(@PathVariable String categoryName) {
		List<Author> authors = adminService.getAuthorByCategoryName(categoryName);
		return ResponseEntity.ok(authors);
	}

	// Update Author
	
	

	// Delete Author
	@DeleteMapping("/authors/{authorId}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<String> deleteAuthor(@PathVariable Long authorId) {
		adminService.deleteAuthor(authorId);
		return ResponseEntity.ok("Author deleted Successfully");
	}

	// ------------------------ Book Management
	// -------------------------------------------------------
	// Adding books
	@PostMapping("/books")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<Book> addBook(@RequestBody Book book, @RequestParam String categoryName) {
		// String categoryName = bookcategory.getName();
		Book addedBook = adminService.addBootToCategoryByName(book, categoryName);
		return ResponseEntity.status(HttpStatus.CREATED).body(addedBook);
	}

	// getBooks
	@GetMapping("/books")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<List<Book>> getBooks() {
		List<Book> booklist = adminService.getBooks();
		return ResponseEntity.ok(booklist);
	}

	// Updating Books
	@PutMapping("/books/{bookId}")
	@PreAuthorize("hasRole('Admin')")
	public ResponseEntity<Book> updatedBook(@PathVariable Long bookId, @RequestBody Book updatedBookInfo,
			@RequestParam(required = false) Long authorId, @RequestParam(required = false) String updatedAuthName,
			@RequestBody(required = false) AdditionalInformation addInfo) {

		Book updatedBook = adminService.updatedBook(bookId, updatedBookInfo, authorId, updatedAuthName, addInfo);
		return ResponseEntity.ok(updatedBook);
	}

	// Delete
	@DeleteMapping("/books/{bookId}")
	public ResponseEntity<String> deleteBooks(@PathVariable Long bookId) {
		adminService.deleteBook(bookId);
		return ResponseEntity.ok("Book deleted Successfully");
	}

}