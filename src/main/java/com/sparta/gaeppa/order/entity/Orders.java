package com.sparta.gaeppa.order.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.store.entity.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Orders extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "store_id")
    private Store store;

    @Embedded
    private Address address;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "order_status")
    private OrderStatus orderStatus;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "order_type")
    private OrderType orderType;

    @Column(nullable = false, name = "order_total_price")
    private int orderTotalPrice;

    @Column(name = "order_request")
    private String orderRequest;

    @OneToMany(mappedBy = "order",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProductList = new ArrayList<>();


    @Builder
    private Orders(Member member, Store store, Address address, OrderType orderType,
                   String orderRequest) {
        this.member = member;
        this.store = store;
        this.address = address;
        this.orderStatus = OrderStatus.COMPLETED;
        this.orderType = orderType;
        this.orderRequest = orderRequest;
    }

    public void putOrderProduct(OrderProduct orderProduct) {
        orderProductList.add(orderProduct);
    }

    public void putTotalPrice(int price) {
        orderTotalPrice = price;
    }

    public void cancel(UUID orderId) {
        this.orderStatus = OrderStatus.CANCELED;
    }
}
