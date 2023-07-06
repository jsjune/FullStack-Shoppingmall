package com.example.be.product.dto;

import com.example.be.product.entity.ImageDao;
import com.example.be.product.entity.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class DetailProductDto {
    private Long id;
    private Long price;
    private Long sold;
    private String description;
    private List<ImageDao> imageUrls;

    public DetailProductDto(Product product) {
        this.id = product.getId();
        this.price = product.getPrice();
        this.sold = product.getSold();
        this.description = product.getDescription();
        this.imageUrls = product.getImageUrls();
    }
}
