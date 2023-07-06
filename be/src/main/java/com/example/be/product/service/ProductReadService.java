package com.example.be.product.service;

import com.example.be.product.dto.DetailProductDto;
import com.example.be.product.dto.response.DetailProductResponse;
import com.example.be.product.dto.response.ProductsResponse;
import com.example.be.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProductReadService {
    private final ProductRepository productRepository;

    public ProductsResponse getProducts(Pageable pageable, List<Integer> continents, List<Integer> price, String searchTerm) {
        return productRepository.getTotalProducts(pageable, continents, price, searchTerm);
    }

    public DetailProductResponse getDetailProduct(String productId, String type) {
        List<Long> productIds = null;
        if (type.equals("single")) {
            productIds = Stream.of(productId.split(",")).map(Long::parseLong).collect(Collectors.toList());
        } else if (type.equals("array")) {
            productIds = Stream.of(productId.split(",")).map(Long::parseLong).collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("잘못된 타입 입니다.");
        }
        List<DetailProductDto> detailProducts = productRepository.findByIdIn(productIds).stream().map(DetailProductDto::new).collect(Collectors.toList());
        return new DetailProductResponse(detailProducts);
    }
}
