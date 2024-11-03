package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.Entity.Book;
import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {

	
}
