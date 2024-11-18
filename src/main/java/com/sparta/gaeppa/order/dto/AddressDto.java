package com.sparta.gaeppa.order.dto;

import com.sparta.gaeppa.order.entity.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class AddressDto {

    private String address;
    private String detailAddress;
    private int postAddress;

    public static AddressDto from(Address address) {
        return AddressDto.builder()
                .address(address.getAddress())
                .detailAddress(address.getDetailAddress())
                .postAddress(address.getPostAddress())
                .build();
    }

    public static Address toEntity(AddressDto addressDto) {
        return Address.builder()
                .address(addressDto.getAddress())
                .detailAddress(addressDto.getDetailAddress())
                .postAddress(addressDto.getPostAddress())
                .build();
    }
}
