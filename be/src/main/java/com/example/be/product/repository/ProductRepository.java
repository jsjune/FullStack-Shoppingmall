package com.example.be.product.repository;

import com.example.be.product.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @EntityGraph(attributePaths = "imageUrls")
    List<Product> findByIdIn(List<Long> productIds);
}

