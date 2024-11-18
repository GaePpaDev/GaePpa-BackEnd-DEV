package com.sparta.gaeppa.product.dto.productOptionCategory;

import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionCategoryPutRequestDto {
    @NotBlank(message = "Product Option Category Name is required")
    private String productOptionCategoryName;
    @Min(value = 1, message = "Min limits should be greater than 0")
    private int maxLimits;

    public ProductOptionCategory toEntity() {
        return ProductOptionCategory.builder()
                .name(productOptionCategoryName)
                .maxLimits(maxLimits)
                .build();
    }
}
