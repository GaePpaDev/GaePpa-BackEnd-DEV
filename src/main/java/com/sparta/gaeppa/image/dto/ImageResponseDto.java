package com.sparta.gaeppa.image.dto;

import com.sparta.gaeppa.image.entity.ProductImage;
import com.sparta.gaeppa.image.entity.ReviewImage;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseDto {
    private UUID imageId;
    private String fileName;

    public static ImageResponseDto from(ProductImage productImage) {
        return ImageResponseDto.builder()
                .imageId(productImage.getId())
                .fileName(productImage.getFileName())
                .build();
    }

    public static ImageResponseDto from(ReviewImage reviewImage) {
        return ImageResponseDto.builder()
                .imageId(reviewImage.getId())
                .fileName(reviewImage.getFileName())
                .build();
    }
}
