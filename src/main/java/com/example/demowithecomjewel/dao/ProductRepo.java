package com.example.demowithecomjewel.dao;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demowithecomjewel.model.Product;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
	// Add this method
//    Optional<Product> findById(String name);
}
