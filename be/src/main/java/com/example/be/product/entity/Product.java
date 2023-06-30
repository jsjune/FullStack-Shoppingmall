package com.example.be.product.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Long price;

    @ElementCollection
    @CollectionTable(name = "PRODUCT_ORIGINAL_IMAGES",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"))
    private Set<ImageDao> imageUrls = new HashSet<>();

    private Long sold;
    private int continents;
    private Long views;

    @Builder
    public Product(Long userId, String title, String description, Long price, Set<ImageDao> imageUrls, Long sold, int continents, Long views) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrls = imageUrls;
        this.sold = sold;
        this.continents = continents;
        this.views = views;
    }
}
