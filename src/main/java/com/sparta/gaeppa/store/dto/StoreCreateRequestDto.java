package com.sparta.gaeppa.store.dto;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.entity.StoreCategory;
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
public class StoreCreateRequestDto {

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

    // Store 엔티티로 변환 (StoreCategory는 별도로 설정)
    public Store toStoreEntity(StoreCategory category, Member owner) {
        return Store.builder()
                .storeName(storeName)
                .storeAddress(storeAddress)
                .storeTelephone(storeTelephone)
                .storeIntroduce(storeIntroduce)
                .businessTime(businessTime)
                .category(category)
                .member(owner)
                .build();
    }

}
