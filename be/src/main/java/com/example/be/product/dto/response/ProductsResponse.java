package com.example.be.product.dto.response;

import com.example.be.product.dto.ProductDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class ProductsResponse {
    private Long totalProductCount;
    private int totalProductPage;
    private List<ProductDto> productList;

    public ProductsResponse(Long totalProductCount, PageImpl<ProductDto> productList) {
        this.totalProductCount = totalProductCount;
        this.totalProductPage = productList.getTotalPages();
        this.productList = productList.getContent();
    }
}
