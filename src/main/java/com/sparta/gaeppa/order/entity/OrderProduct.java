package com.sparta.gaeppa.order.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.product.entity.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_product_id")
    private UUID orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    @Column(nullable = false, name = "order_product_name")
    private String orderProductName;

    @Column(nullable = false, name = "order_product_price")
    private int orderProductPrice;

    @Column(nullable = false, name = "order_product_quantity")
    private int orderProductQuantity;

    @OneToMany(mappedBy = "orderProduct",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderOption> orderOptionList = new ArrayList<>();

    @Builder
    public OrderProduct(Orders order, Product product, String orderProductName, int orderProductPrice,
                        int orderProductQuantity) {
        this.order = order;
        this.product = product;
        this.orderProductName = orderProductName;
        this.orderProductPrice = orderProductPrice;
        this.orderProductQuantity = orderProductQuantity;
    }


    public void putOrderOption(OrderOption orderOption) {
        this.orderOptionList.add(orderOption);
    }

}
