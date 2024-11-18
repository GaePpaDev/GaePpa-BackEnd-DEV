package com.sparta.gaeppa.image.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.global.storage.StorageService;
import com.sparta.gaeppa.image.dto.ImageResponseDto;
import com.sparta.gaeppa.image.dto.ResourceDto;
import com.sparta.gaeppa.image.entity.ProductImage;
import com.sparta.gaeppa.image.mime.MimeType;
import com.sparta.gaeppa.image.repository.ProductImageRepository;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.repository.ProductRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductImageService {

    private final StorageService storageService;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    private static final Path PRODUCT_IMAGE_PATH = Path.of("product-images");

    public ProductImageService(StorageService storageService, ProductImageRepository productImageRepository,
                               ProductRepository productRepository) {
        this.storageService = storageService;
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.storageService.createDirectory(PRODUCT_IMAGE_PATH);
    }
    
    public ImageResponseDto uploadProductImage(String productId, MultipartFile file) {

        Product product = productRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_NOT_FOUND));

        String fileName = productId + "-" + UUID.randomUUID() + "-" + System.currentTimeMillis();

        storageService.store(file, PRODUCT_IMAGE_PATH, fileName);

        ProductImage productImage = ProductImage.builder()
                .product(product)
                .originalName(file.getOriginalFilename())
                .fileName(fileName)
                .mimeType(MimeType.valueOf(file.getContentType()))
                .build();

        return ImageResponseDto.from(productImageRepository.save(productImage));
    }

    public ResourceDto getProductImage(UUID imageId) {

        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_IMAGE_NOT_FOUND));

        Resource resource = storageService.loadAsResource(PRODUCT_IMAGE_PATH + "/" + productImage.getFileName());

        return ResourceDto.builder()
                .resource(resource)
                .mimeType(productImage.getMimeType()).build();
    }

    public void deleteProductImage(UUID imageId, CustomUserDetails userDetails) {

        ProductImage productImage = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_IMAGE_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MANAGER && userDetails.getMemberRole() != MemberRole.OWNER
                && !productImage.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        productImage.delete(userDetails.getUsername());
    }
}
