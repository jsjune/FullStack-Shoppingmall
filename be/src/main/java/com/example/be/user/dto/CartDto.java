package com.example.be.user.dto;

import com.example.be.user.entity.Cart;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CartDto {
    private Long productId;
    private int quantity;
    private LocalDateTime date;

    public CartDto(Cart cart) {
        this.productId = cart.getProductId();
        this.quantity = cart.getQuantity();
        this.date = cart.getDate();
    }
}
