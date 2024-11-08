package com.sparta.gaeppa.cart.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderProducts extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_product_id")
    private UUID orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private Orders order;

    @Column(nullable = false, name = "order_product_name")
    private String orderProductName;

    @Column(nullable = false, name = "order_product_price")
    private int orderProductPrice;

    @Column(nullable = false, name = "order_product_quantity")
    private int orderProductQuantity;


}
