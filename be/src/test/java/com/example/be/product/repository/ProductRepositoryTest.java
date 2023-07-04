package com.example.be.product.repository;

import com.example.be.common.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;



    @DisplayName("상품 페이징 처리하여 조회하기")
    @Test
    void test() {

    }


}
