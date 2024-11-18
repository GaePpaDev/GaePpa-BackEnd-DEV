package com.sparta.gaeppa.order.dto;


import com.sparta.gaeppa.order.entity.Orders;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class OrderResponseDto {

    private UUID orderId;
    private UUID storeId;
    private String storeName;
    private AddressDto address;
    private String orderStatus;
    private String orderType;
    private int totalPrice;
    private String orderRequest;
    private List<OrderProductDto> orderProductList;

    public static OrderResponseDto from(Orders orders) {
        return OrderResponseDto.builder()
                .orderId(orders.getOrderId())
                .storeId(orders.getStore().getStoreId())
                .storeName(orders.getStore().getStoreName())
                .address(AddressDto.from(orders.getAddress()))
                .orderStatus(orders.getOrderStatus().toString())
                .orderType(orders.getOrderType().toString())
                .totalPrice(orders.getOrderTotalPrice())
                .orderRequest(orders.getOrderRequest())
                .orderProductList(orders.getOrderProductList().stream().map(OrderProductDto::from).toList())
                .build();
    }

}
