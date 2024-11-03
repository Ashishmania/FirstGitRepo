package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.Entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

}
