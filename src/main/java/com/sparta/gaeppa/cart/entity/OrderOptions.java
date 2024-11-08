package com.sparta.gaeppa.cart.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderOptions extends BaseEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_product_id")
    private OrderProducts orderProduct;

    @Column(nullable = false, name = "option_name")
    private String optionName;

    @Column(nullable = false, name = "option_price")
    private String optionPrice;

}
