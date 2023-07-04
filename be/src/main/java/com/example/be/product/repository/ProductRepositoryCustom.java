package com.example.be.product.repository;

import com.example.be.product.dto.response.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    ProductResponse getTotalProducts(Pageable pageable, List<Integer> continents, List<Integer> price, String searchTerm);
}
