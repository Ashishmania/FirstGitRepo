package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.Entity.Distributor;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {

}
