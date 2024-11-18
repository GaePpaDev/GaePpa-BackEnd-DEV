package com.sparta.gaeppa.product.dto.productCategory;

import com.sparta.gaeppa.product.entity.ProductCategory;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategoryResponseDto {

    private UUID id;
    private String name;

    @Builder
    private ProductCategoryResponseDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProductCategoryResponseDto from(ProductCategory productCategory) {
        return ProductCategoryResponseDto.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .build();
    }
}
