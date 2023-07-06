package com.example.be.product.dto.response;

import com.example.be.product.dto.DetailProductDto;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailProductResponse {
    private List<DetailProductDto> products;

    public DetailProductResponse(List<DetailProductDto> products) {
        this.products = products;
    }
}
