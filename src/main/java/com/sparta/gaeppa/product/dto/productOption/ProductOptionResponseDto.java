package com.sparta.gaeppa.product.dto.productOption;

import com.sparta.gaeppa.product.entity.ProductOption;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionResponseDto {
    private UUID productOptionId;
    private String optionName;
    private int optionPrice;

    @Builder
    private ProductOptionResponseDto(UUID productOptionId, String optionName, int optionPrice) {
        this.productOptionId = productOptionId;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }

    public static ProductOptionResponseDto from(ProductOption productOption) {
        return ProductOptionResponseDto.builder()
                .productOptionId(productOption.getId())
                .optionName(productOption.getName())
                .optionPrice(productOption.getPrice())
                .build();
    }
}
