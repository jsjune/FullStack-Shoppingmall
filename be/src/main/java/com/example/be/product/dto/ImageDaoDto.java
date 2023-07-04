package com.example.be.product.dto;

import lombok.Getter;

@Getter
public class ImageDaoDto {
    private String orgImageUrl;
    private String thumbImageUrl;

    public ImageDaoDto(String orgImageUrl, String thumbImageUrl) {
        this.orgImageUrl = orgImageUrl;
        this.thumbImageUrl = thumbImageUrl;
    }
}
