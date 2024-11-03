package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Entity.AdditionalInformation;

public interface AdditionalInfoRepo extends JpaRepository<AdditionalInformation, Long> {

}
