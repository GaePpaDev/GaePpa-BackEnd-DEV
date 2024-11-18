package com.sparta.gaeppa.product.dto.productOption;

import com.sparta.gaeppa.product.entity.ProductOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOptionRequestDto {
    @NotBlank(message = "Product Option Category Id is required")
    private UUID productOptionCategoryId;
    @NotBlank(message = "Product Option Name is required")
    private String name;
    @NotNull(message = "Product Option Price is required")
    private int price;

    public ProductOption toEntity() {
        return ProductOption.builder()
                .name(name)
                .price(price)
                .build();
    }
}
