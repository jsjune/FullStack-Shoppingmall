package com.example.be.user.dto.response;

import com.example.be.user.dto.CartDto;
import lombok.Getter;

import java.util.List;

@Getter
public class CartResponse {
    private List<CartDto> carts;

    public CartResponse(List<CartDto> carts) {
        this.carts = carts;
    }
}
