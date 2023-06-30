package com.example.be.product.controller;

import com.example.be.product.service.ProductWriteService;
import com.example.be.product.dto.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductWriteService productService;

    @PostMapping
    public void productRegister(@ModelAttribute ProductRequest request) {
        productService.productRegister(request);
    }

}
