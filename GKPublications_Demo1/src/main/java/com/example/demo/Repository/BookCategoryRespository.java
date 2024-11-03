package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.Entity.BookCategory;
import java.util.List;
import java.util.Optional;


public interface BookCategoryRespository extends JpaRepository<BookCategory, Long> {
	
	Optional<BookCategory> findByName(String name);

	
	
}
