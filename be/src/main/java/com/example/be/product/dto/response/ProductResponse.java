package com.example.be.product.dto.response;

import com.example.be.product.dto.ProductListDto;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Getter
public class ProductResponse {
    private Long totalProductCount;
    private int totalProductPage;
    private List<ProductListDto> productList;

    public ProductResponse(Long totalProductCount, PageImpl<ProductListDto> productList) {
        this.totalProductCount = totalProductCount;
        this.totalProductPage = productList.getTotalPages();
        this.productList = productList.getContent();
    }
}
