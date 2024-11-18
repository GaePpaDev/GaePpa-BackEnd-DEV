package com.sparta.gaeppa.order.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderOption extends BaseEntity {

    @Id
    @Column(name = "option_id")
    private UUID optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @Column(nullable = false, name = "option_name")
    private String optionName;

    @Column(nullable = false, name = "option_price")
    private int optionPrice;


    @Builder
    private OrderOption(OrderProduct orderProduct, String optionName, int optionPrice) {
        this.orderProduct = orderProduct;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }
}
