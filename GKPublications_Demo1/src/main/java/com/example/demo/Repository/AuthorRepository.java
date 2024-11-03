package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.Author;
import com.example.demo.Entity.BookCategory;
import java.util.*;


public interface AuthorRepository extends JpaRepository<Author, Long> {
	
	List<Author> findByCategory(BookCategory category);

}
