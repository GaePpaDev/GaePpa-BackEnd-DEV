package com.sparta.gaeppa.order.dto;

import com.sparta.gaeppa.order.entity.Orders;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderListResponseDto {

    private List<OrderResponseDto> orders;

    public OrderListResponseDto(List<Orders> orders) {
        this.orders = orders.stream().map(OrderResponseDto::from).toList();
    }

}
