package com.example.be.product.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductRequest {
    private Long writer;
    private String title;
    private String description;
    private Long price;
    private int continents;
    private List<MultipartFile> file;
}
