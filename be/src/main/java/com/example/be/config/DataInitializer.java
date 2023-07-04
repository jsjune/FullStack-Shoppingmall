package com.example.be.config;

import com.example.be.product.entity.ImageDao;
import com.example.be.product.entity.Product;
import com.example.be.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final ProductRepository productRepository;

    @PostConstruct
    public void initializeData() {

        List<ImageDao> images = new ArrayList<>();
        images.add(new ImageDao("https://jsj-bucket.s3.ap-northeast-2.amazonaws.com/images/2023/06-30/5c1c513b-bc3c-499b-b58b-fbf9fe9384e5_aws_cover.png", "https://jsj-bucket.s3.ap-northeast-2.amazonaws.com/images/2023/06-30/6ec5cd74-5cc5-42f9-aa41-1947821e31a9_http_logo.jpg"));
        images.add(new ImageDao("https://jsj-bucket.s3.ap-northeast-2.amazonaws.com/images/2023/06-30/6ec5cd74-5cc5-42f9-aa41-1947821e31a9_http_logo.jpg", ""));

        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            StringBuilder randomTitle = new StringBuilder();
            long randomPrice = (long) (random.nextDouble() * 10000L);
            int randomContinent = random.nextInt(7) + 1;

            for (int j = 0; j < 5; j++) { // 랜덤한 길이로 문자열 생성 (예시에서는 길이 5로 설정)
                int index = random.nextInt(characters.length());
                randomTitle.append(characters.charAt(index)); // 랜덤한 문자 선택하여 문자열에 추가
            }
            Product product = Product.builder()
                    .userId(1L)
                    .title(randomTitle.toString())
                    .description("bbb")
                    .price(randomPrice)
                    .imageUrls(images)
                    .sold(0L)
                    .continents(randomContinent)
                    .views(0L)
                    .build();
            products.add(product);
        }
        productRepository.saveAll(products);
    }
}
