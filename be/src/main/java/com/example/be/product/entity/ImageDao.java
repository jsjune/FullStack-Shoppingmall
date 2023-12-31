package com.example.be.product.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ImageDao {
    private String orgImageUrl;
    private String thumbImageUrl;

    public ImageDao(String orgImageUrl, String thumbImageUrl) {
        this.orgImageUrl = orgImageUrl;
        this.thumbImageUrl = thumbImageUrl;
    }
}
