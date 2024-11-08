package com.sparta.gaeppa.cart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    @Column(nullable = false)
    private String address;
    @Column(name = "detail_address")
    private String detailAddress;
    @Column(name = "post_address")
    private int postAddress;
    @Column(name = "delivery_request")
    private String deliveryRequest;
}
