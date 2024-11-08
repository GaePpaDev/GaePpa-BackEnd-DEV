package com.sparta.gaeppa.cart.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;

@Entity
@Table(name = "p_payments")
@Getter
public class Payments extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "pay_id")
    private UUID payId;

    @OneToOne
    @JoinColumn(nullable = false, name = "order_id")
    private Orders order;

    @Column(nullable = false, name = "pay_staus")
    private String payStatus;

    @Column(nullable = false, name = "pay_transcation_id")
    private String payTransactionId;

}
