package com.example.be.user.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {
    private Long productId;
    private int quantity;
    private LocalDateTime date;

    public Cart(Long productId, int quantity, LocalDateTime date) {
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
    }

    public void addQuantity() {
        this.quantity++;
    }
}
