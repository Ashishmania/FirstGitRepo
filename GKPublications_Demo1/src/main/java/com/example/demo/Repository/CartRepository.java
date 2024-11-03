package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.Entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
