package com.example.be.product.service;

import com.example.be.common.exception.GlobalException;
import com.example.be.config.s3.S3Service;
import com.example.be.product.dto.request.ProductRequest;
import com.example.be.product.entity.ImageDao;
import com.example.be.product.entity.Product;
import com.example.be.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.example.be.common.exception.ErrorCode.NOT_IMAGE_FILE;

@Service
@RequiredArgsConstructor
public class ProductWriteService {
    private final ProductRepository productRepository;
    private final S3Service s3Service;
    private final static String UPLOAD_FOLDER = "images";

    public void productRegister(ProductRequest request) {
        // 이미지 파일 업로드 x
        if (Objects.isNull(request.getFile())) {
            System.out.println("이미지 없다");
        } else {
            // 이미지 파일 업로드 O
            Set<ImageDao> savedImages = new HashSet<>();
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

        }
    }


}
