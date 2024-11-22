package com.sparta.gaeppa.order.dto;

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
public class OrderAndPaymentRequestDto {

    private UUID memberId;
    private UUID storeId;
    private AddressDto address;
    private String orderType;
    private String orderRequest;
    private List<OrderProductDto> orderProductList;

    private String payStatus;
    private String payType;
    private String payTransactionCode;


    public void setMemberIdByUserDetail(UUID memberId) {
        this.memberId = memberId;
    }
}
