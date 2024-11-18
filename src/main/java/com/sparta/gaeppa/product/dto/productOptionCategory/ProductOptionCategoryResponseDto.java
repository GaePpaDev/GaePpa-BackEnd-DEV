package com.sparta.gaeppa.product.dto.productOptionCategory;

import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionCategoryResponseDto {
    private UUID productOptionCategoryId;
    private String productOptionCategoryName;
    private int maxLimits;

    @Builder
    private ProductOptionCategoryResponseDto(UUID productOptionCategoryId, String productOptionCategoryName,
                                             int maxLimits) {
        this.productOptionCategoryId = productOptionCategoryId;
        this.productOptionCategoryName = productOptionCategoryName;
        this.maxLimits = maxLimits;
    }

    public static ProductOptionCategoryResponseDto from(ProductOptionCategory productOptionCategory) {
        return ProductOptionCategoryResponseDto.builder()
                .productOptionCategoryId(productOptionCategory.getId())
                .productOptionCategoryName(productOptionCategory.getName())
                .maxLimits(productOptionCategory.getMaxLimits())
                .build();
    }
}
