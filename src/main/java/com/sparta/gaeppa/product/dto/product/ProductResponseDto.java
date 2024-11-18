package com.sparta.gaeppa.product.dto.product;

import com.sparta.gaeppa.product.entity.Product;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private String productDescription;
    private int productPrice;

    @Builder
    private ProductResponseDto(UUID productId, String productName, String productDescription, int productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public static ProductResponseDto from(Product product) {
        return ProductResponseDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productPrice(product.getPrice())
                .build();
    }
}
