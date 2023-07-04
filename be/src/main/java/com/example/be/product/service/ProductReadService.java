package com.example.be.product.service;

import com.example.be.product.dto.response.ProductResponse;
import com.example.be.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReadService {
    private final ProductRepository productRepository;

    public ProductResponse getProduct(Pageable pageable, List<Integer> continents, List<Integer> price, String searchTerm) {
        return productRepository.getTotalProducts(pageable, continents, price, searchTerm);
    }
}
