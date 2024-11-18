package com.sparta.gaeppa.product.dto.product;

import com.sparta.gaeppa.product.entity.Product;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    @NotBlank(message = "Product Category Id is required")
    private UUID productCategoryId;
    @NotBlank(message = "Product Name is required")
    private String productName;
    @NotBlank(message = "Product Price is required")
    private int productPrice;
    @NotBlank(message = "Product Description is required")
    private String productDescription;
    private boolean hideStatus;

    public Product toEntity() {
        return Product.builder()
                .name(productName)
                .price(productPrice)
                .description(productDescription)
                .hideStatus(hideStatus)
                .build();
    }
}
