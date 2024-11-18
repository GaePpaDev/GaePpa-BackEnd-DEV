package com.sparta.gaeppa.product.dto.product;

import com.sparta.gaeppa.product.entity.ProductCategory;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreProductListResponseDto {
    private int totalAmount;
    private List<CategoryResponseDto> details;

    public StoreProductListResponseDto(int totalAmount, List<ProductCategory> categories) {
        this.totalAmount = totalAmount;
        this.details = categories.stream().map(CategoryResponseDto::from).toList();
    }

    @Getter
    private static class CategoryResponseDto {
        private final String category;
        private final List<ProductResponseDto> products;


        @Builder
        private CategoryResponseDto(String category, List<ProductResponseDto> products) {
            this.category = category;
            this.products = products;
        }

        public static CategoryResponseDto from(ProductCategory productCategory) {
            return CategoryResponseDto.builder()
                    .category(productCategory.getName())
                    .products(productCategory.getProducts().stream().map(ProductResponseDto::from).toList())
                    .build();
        }


    }
}
