package com.example.be.product.service;

import com.example.be.common.exception.GlobalException;
import com.example.be.config.s3.S3Service;
import com.example.be.product.dto.request.ProductRequest;
import com.example.be.product.entity.ImageDao;
import com.example.be.product.entity.Product;
import com.example.be.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.be.common.exception.ErrorCode.NOT_IMAGE_FILE;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;
    private final static String UPLOAD_FOLDER = "images";

    public void productRegister(ProductRequest request) {
        // 이미지 파일 업로드 x
        if (Objects.isNull(request.getFile())) {
            Product product = Product.builder()
                    .userId(request.getWriter())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .sold(0L)
                    .continents(request.getContinents())
                    .views(0L)
                    .build();
            productRepository.save(product);
        } else {
            // 이미지 파일 업로드 O
            List<ImageDao> savedImages = new ArrayList<>();
            for (MultipartFile image : request.getFile()) {
                if (!Objects.requireNonNull(image.getContentType()).startsWith("image")) {
                    throw new GlobalException(NOT_IMAGE_FILE);
                }
                String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));
                String uploadFolder = UPLOAD_FOLDER + "/" + datePath;
                String uploadImageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                String s3Path = uploadFolder + "/" + uploadImageName;
                String orgImageUrl = s3Service.uploadFile(image, s3Path);

                String thumbS3Path = uploadFolder + "/s_" + uploadImageName;
                String thumbImageUrl = s3Service.uploadThumbFile(image, thumbS3Path);

                savedImages.add(new ImageDao(orgImageUrl, thumbImageUrl));
            }
            Product product = Product.builder()
                    .userId(request.getWriter())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .imageUrls(savedImages)
                    .sold(0L)
                    .continents(request.getContinents())
                    .views(0L)
                    .build();
            productRepository.save(product);
        }
    }

}
