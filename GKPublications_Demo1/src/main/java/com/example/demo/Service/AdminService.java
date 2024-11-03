package com.example.demo.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.example.demo.Entity.AdditionalInformation;
import com.example.demo.Entity.Author;
import com.example.demo.Entity.Book;
import com.example.demo.Entity.BookCategory;
import com.example.demo.Repository.AuthorRepository;
import com.example.demo.Repository.BookCategoryRespository;
import com.example.demo.Repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final AuthorRepository authorRepository;
	private final BookCategoryRespository bookCategoryRespository;
	private final BookRepository bookRepository;

	// ================== Book Category Management ============================

	// 1.) Add a Bookcategory ==== 
	public BookCategory addBookCategory(String Name) {
		BookCategory category = new BookCategory();
		category.setName(Name);
		return bookCategoryRespository.save(category);
	}

	// 2.) Admin can view list of categories ====
	public List<BookCategory> getAllCategories() {
		return bookCategoryRespository.findAll();
	}

	// ======== getting category by ID ===========
	public Optional<BookCategory> getCategoryById(Long id) {
		return bookCategoryRespository.findById(id);
	}

	// ========= getting category by Name ==================
	public Optional<BookCategory> getCategoryByName(String Name) {
		return bookCategoryRespository.findByName(Name);
	}

	// 3.) Update BookCategory by Name** ====
	public BookCategory updateBookCategoryByName(String Name, String newName) {
		Optional<BookCategory> bookcategory = bookCategoryRespository.findByName(Name);
		if (bookcategory.isPresent()) {
			BookCategory category = bookcategory.get();
			category.setName(newName);
			return bookCategoryRespository.save(category);
		} else {
			throw new RuntimeException("Category Not found with Name:" + Name);
		}
	}

	// ========== Update BookCategory by ID** =====================
	public BookCategory updateBookCategoryById(Long catId, String newName) {
		Optional<BookCategory> categoryOpt = bookCategoryRespository.findById(catId);
		if (categoryOpt.isPresent()) {
			BookCategory category = categoryOpt.get();
			category.setName(newName);
			return bookCategoryRespository.save(category);
		} else {
			throw new RuntimeException("Category not found with ID:" + catId);
		}
	}

	//4.) ======= Delete BookCategoryByName =======
	public void deleteBookCategoryByName(String catgoryName) {
		Optional<BookCategory> categoryOpt = bookCategoryRespository.findByName(catgoryName);
		if (categoryOpt.isPresent()) {
			bookCategoryRespository.delete(categoryOpt.get());
		} else {
			throw new RuntimeException("Category not found with Name:" + catgoryName);
		}
	}

	// =================== Author Management ====================================

	// 5.) === Adding Author
	public Author addAuthor(String authorName) {
		Author author = new Author();
		author.setName(authorName);
		return authorRepository.save(author);
	}

	// 6.) == View Authorlist
	public List<Author> getAuthors() {
		return authorRepository.findAll();
	}

	// 7.) viewAuthorByCategoryName**
	public List<Author> getAuthorByCategoryName(String categoryName) {
		Optional<BookCategory> categoryOpt = bookCategoryRespository.findByName(categoryName);
		if (categoryOpt.isPresent()) {
			BookCategory bookcategory = categoryOpt.get();
			return authorRepository.findByCategory(bookcategory);
		} else {
			throw new RuntimeException("Category Not found with Name:" + categoryName);
		}
	}

	// 8.) ==== Assigning a category to an existing author by CategoryId***
	public Author assignCategoryToAuthorById(Long authorId, Long categoryID) {
		Optional<Author> author = authorRepository.findById(authorId);
		Optional<BookCategory> bookcateory = bookCategoryRespository.findById(categoryID);

		// first checking in the DB ki author aur bookcategory dono present hai,
		// agr hai to author ko wo category assign kr denge

		if (author.isPresent() && bookcateory.isPresent()) {
			author.get().setCategory(bookcateory.get());
			return authorRepository.save(author.get());
		} else {
			throw new RuntimeException("Author or Category Not found");
		}
	}

	// ===== Assigning a category to an existing author by CategoryName****
	public Author assignCategoryToAuthorByName(Long authorID, String categoryName) {
		Optional<Author> author = authorRepository.findById(authorID);
		Optional<BookCategory> category = bookCategoryRespository.findByName(categoryName);

		if (author.isPresent() && category.isPresent()) {
			author.get().setCategory(category.get());
			return authorRepository.save(author.get());
		}else {
			throw new RuntimeException("Author or Category Not Found");
		}

	}

	// 9.) ==== Delete Author
	public void deleteAuthor(Long authorId) {
		Optional<Author> authorOpt = authorRepository.findById(authorId);
		if (authorOpt.isPresent()) {
			authorRepository.delete(authorOpt.get());
		} else {
			throw new RuntimeException("Author is not present with Id :" + authorId);
		}

	}

	// ================= Book Management
	// ===================================================

	// 10.)  == Add book to a category
	public Book addBookToCategoryById(Book book, Long categoryId) {
		Optional<BookCategory> categoryOpt = getCategoryById(categoryId);
		if (categoryOpt.isPresent()) {
			book.setCategory(categoryOpt.get());
			return bookRepository.save(book);
		} else {
			throw new RuntimeException("Category not found with ID: " + categoryId);
		}
	}

	public Book addBootToCategoryByName(Book book, String categoryName) {
		Optional<BookCategory> categoryOpt = getCategoryByName(categoryName);
		if (categoryOpt.isPresent()) {
			book.setCategory(categoryOpt.get());
			return bookRepository.save(book);
		} else {
			throw new RuntimeException("category Not found with Name:" + categoryName);
		}

	}

	//11.)  == view Books
	public List<Book> getBooks() {
		return bookRepository.findAll();
	}

	//12.)  Update Book
	public Book updatedBook(Long bookId, Book updatedBookinfo, Long authorId, String updatedAuthName,
			AdditionalInformation addInfo) {
		Optional<Book> bookOpt = bookRepository.findById(bookId);

		if (bookOpt.isPresent()) {
			Book book = bookOpt.get();
			
			// Updation of book fields
			book.setTitle(updatedBookinfo.getTitle());
			book.setLanguage(updatedBookinfo.getLanguage());
			book.setPrice(updatedBookinfo.getPrice());

			// updating book author
			if (authorId != null) {
				Optional<Author> authorOpt = authorRepository.findById(authorId);

				if (authorOpt.isPresent()) {
					Author author = authorOpt.get();

					// if we have to update the author name as well
					if (updatedAuthName != null && !updatedAuthName.isEmpty()) {
						author.setName(updatedAuthName);
						authorRepository.save(author);
					}

					book.setAuthor(author);
				} else {
					throw new RuntimeException("Author not found with Id :" + authorId);
				}
			}

			// updating additionalInfo as well
			if (addInfo != null) {
				AdditionalInformation addinformation = book.getAdditionalInformation();

				if (addinformation == null) {
					addinformation = new AdditionalInformation();
				}

				// addInfo k fields ko addinformation m copy krenge **** 
				addinformation.setWeight(addInfo.getWeight());
				addinformation.setDimensions(addInfo.getDimensions());
				addinformation.setPublisher(addInfo.getPublisher());
				addinformation.setLanguage(addInfo.getLanguage());
				addinformation.setPublishingMonth(addInfo.getPublishingMonth());
				addinformation.setPublishingYear(addInfo.getPublishingYear());
				addinformation.setTotalPages(addInfo.getTotalPages());
				addinformation.setTotalPages(addInfo.getTotalPages());
				addinformation.setEdition(addInfo.getEdition());
				addinformation.setBuyFromAmazonLink(addInfo.getBuyFromAmazonLink());

				// setting the updated addinformation in book object
				book.setAdditionalInformation(addinformation);

			}
			return bookRepository.save(book);

		} else {
			throw new RuntimeException("Book Not found with Id: " + bookId);
		}
	}

	// 13.) == Delete Book
	public void deleteBook(Long bookId) {
		Optional<Book> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			bookRepository.delete(bookOpt.get());
		} else {
			throw new RuntimeException("Book not found with Id" + bookId);
		}
	}

	
	
}
