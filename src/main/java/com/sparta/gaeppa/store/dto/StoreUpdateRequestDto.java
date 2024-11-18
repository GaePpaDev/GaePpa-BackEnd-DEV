package com.sparta.gaeppa.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StoreUpdateRequestDto {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String storeCategoryName;

    @NotBlank(message = "가게 이름은 필수입니다.")
    private String storeName;

    @NotBlank(message = "주소는 필수입니다.")
    private String storeAddress;

    @NotBlank(message = "전화번호는 필수입니다.")
    private String storeTelephone;

    private String storeIntroduce;

    private String businessTime;

}
