package com.sparta.gaeppa.order.dto;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.order.entity.OrderType;
import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.store.entity.Store;
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
public class OrderRequestDto {

    private UUID memberId;
    private UUID storeId;
    private Store store;
    private AddressDto address;
    private String orderType;
    private String orderRequest;
    private List<OrderProductDto> orderProductList;

    public static OrderRequestDto from(OrderAndPaymentRequestDto orderAndPaymentRequestDto) {
        return OrderRequestDto.builder()
                .memberId(orderAndPaymentRequestDto.getMemberId())
                .storeId(orderAndPaymentRequestDto.getStoreId())
                .address(orderAndPaymentRequestDto.getAddress())
                .orderType(orderAndPaymentRequestDto.getOrderType())
                .orderRequest(orderAndPaymentRequestDto.getOrderRequest())
                .orderProductList(orderAndPaymentRequestDto.getOrderProductList())
                .build();
    }

    public Orders toEntity(Member member) {
        return Orders.builder()
                .member(member)
                .store(this.store)
                .address(AddressDto.toEntity(this.address))
                .orderType(OrderType.valueOf(this.orderType))
                .orderRequest(this.orderRequest)
                .build();
    }

    public void putStore(Store store) {
        this.store = store;
    }

}
