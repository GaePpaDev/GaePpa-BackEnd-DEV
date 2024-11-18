package com.sparta.gaeppa.payment.dto;

import com.sparta.gaeppa.order.dto.OrderAndPaymentRequestDto;
import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.payment.entity.Payments;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PaymentDto {

    @Setter
    private UUID orderId;
    private String payStatus;
    private String payType;
    private String payTransactionCode;

    public static PaymentDto from(Payments payments) {
        return PaymentDto.builder()
                .orderId(payments.getOrder().getOrderId())
                .payStatus(payments.getPayStatus().toString())
                .payType(payments.getPayType())
                .payTransactionCode(payments.getPayTransactionCode())
                .build();
    }

    public static PaymentDto from(OrderAndPaymentRequestDto OrderAndPaymentRequestDto) {
        return PaymentDto.builder()
                .payStatus(OrderAndPaymentRequestDto.getPayStatus())
                .payType(OrderAndPaymentRequestDto.getPayType())
                .payTransactionCode(OrderAndPaymentRequestDto.getPayTransactionCode())
                .build();
    }

    public Payments toEntity(Orders orders) {
        return Payments.builder()
                .order(orders)
                .payType(payType)
                .payTransactionCode(payTransactionCode)
                .build();
    }


}
