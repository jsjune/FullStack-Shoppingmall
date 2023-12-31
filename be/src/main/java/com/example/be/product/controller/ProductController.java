package com.example.be.product.controller;

import com.example.be.common.dto.ResponseDto;
import com.example.be.product.dto.request.ProductRequest;
import com.example.be.product.dto.response.DetailProductResponse;
import com.example.be.product.dto.response.ProductsResponse;
import com.example.be.product.service.ProductReadService;
import com.example.be.product.service.ProductWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductWriteService productWriteService;
    private final ProductReadService productReadService;

    @PostMapping
    public void productRegister(@ModelAttribute ProductRequest request) {
        productWriteService.productRegister(request);
    }

    @GetMapping
    public ResponseDto<ProductsResponse> getProducts(Pageable pageable,
                                                     @RequestParam(required = false) List<Integer> continents,
                                                     @RequestParam(required = false) List<Integer> price,
                                                     @RequestParam(required = false) String searchTerm) {
        return ResponseDto.success(productReadService.getProducts(pageable, continents, price, searchTerm));
    }

    @GetMapping("/cart/{productId}")
    public ResponseDto<DetailProductResponse> getDetailProduct(@PathVariable("productId") String productId, @RequestParam("type") String type) {
        return ResponseDto.success(productReadService.getDetailProduct(productId, type));
    }

}
