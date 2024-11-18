package com.sparta.gaeppa.order.dto;

import com.sparta.gaeppa.order.entity.OrderOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderProductOptionDto {

    private String optionName;
    private int optionPrice;

    public OrderOption toEntity() {
        return OrderOption.builder()
                .optionName(this.optionName)
                .optionPrice(this.optionPrice)
                .build();
    }

    public static OrderProductOptionDto from(OrderOption option) {
        return OrderProductOptionDto.builder()
                .optionName(option.getOptionName())
                .optionPrice(option.getOptionPrice())
                .build();
    }
}
