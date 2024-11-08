package com.sparta.gaeppa.cart.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private UUID orderId;

    @Column(nullable = false, name = "member_id")
    private UUID memberId;

    @Column(nullable = false, name = "store_id")
    private UUID storeId;

    @Embedded
    private Address address;

    @Column(nullable = false, name = "order_status")
    private String orderStatus;

    @Column(nullable = false, name = "order_type")
    private String orderType;

    @Column(nullable = false, name = "order_total_price")
    private int orderTotalPrice;

    @Column(name = "order_request")
    private String orderRequest;

}
