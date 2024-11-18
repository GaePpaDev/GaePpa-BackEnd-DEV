package com.sparta.gaeppa.payment.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.order.entity.Orders;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_id")
    private UUID payId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private Orders order;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "pay_status")
    private PaymentStatus payStatus;

    @Column(nullable = false, name = "pay_type")
    private String payType;

    @Column(nullable = false, name = "pay_transaction_code")
    private String payTransactionCode;

    @Builder
    private Payments(Orders order, String payType, String payTransactionCode) {
        this.order = order;
        this.payStatus = PaymentStatus.COMPLETED;
        this.payType = payType;
        this.payTransactionCode = payTransactionCode;
    }

    public void cancel(UUID payId) {
        this.payStatus = PaymentStatus.CANCELED;
    }
}
