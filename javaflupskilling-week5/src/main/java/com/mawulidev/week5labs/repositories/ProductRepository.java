package com.mawulidev.week5labs.repositories;

import com.mawulidev.week5labs.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
