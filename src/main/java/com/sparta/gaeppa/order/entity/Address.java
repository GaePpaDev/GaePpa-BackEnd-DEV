package com.sparta.gaeppa.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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
