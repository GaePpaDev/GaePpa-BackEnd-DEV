package com.sparta.gaeppa.product.dto.productOption;

import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionListResponseDto {
    private int optionCategoryAmount;
    private List<ProductOptionCategoryResponseDto> options;

    @Builder
    private ProductOptionListResponseDto(int optionCategoryAmount, List<ProductOptionCategoryResponseDto> options) {
        this.optionCategoryAmount = optionCategoryAmount;
        this.options = options;
    }

    public static ProductOptionListResponseDto from(List<ProductOptionCategory> productOptionCategories) {
        return ProductOptionListResponseDto.builder()
                //TODO: 상품 카테고리의 갯수를 받아오는 로직 점검 필요
                .optionCategoryAmount(productOptionCategories.size())
                .options(productOptionCategories.stream().map(ProductOptionCategoryResponseDto::from).toList())
                .build();
    }

    @Getter
    private static class ProductOptionCategoryResponseDto {
        private final String optionCategory;
        private final List<ProductOptionResponseDto> options;

        @Builder
        private ProductOptionCategoryResponseDto(String optionCategory, List<ProductOptionResponseDto> productOptions) {
            this.optionCategory = optionCategory;
            this.options = productOptions;
        }

        public static ProductOptionCategoryResponseDto from(ProductOptionCategory productOptionCategory) {
            return ProductOptionCategoryResponseDto.builder()
                    .optionCategory(productOptionCategory.getName())
                    .productOptions(
                            productOptionCategory.getProductOptions().stream().map(ProductOptionResponseDto::from)
                                    .toList())
                    .build();
        }
    }
}
