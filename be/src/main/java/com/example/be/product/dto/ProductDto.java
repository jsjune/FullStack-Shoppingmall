package com.example.be.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String title;
    private int continents;
    private Long price;
    private List<ImageDaoDto> imageUrls;

    public ProductDto(Long id, String title, int continents, Long price) {
        this.id = id;
        this.title = title;
        this.continents = continents;
        this.price = price;
    }
}
