package com.sparta.gaeppa.store.dto;

import com.sparta.gaeppa.store.entity.Store;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class StoreResponseDto {

    private UUID storeId;         // 가게 ID
    private String storeName;     // 가게 이름
    private String storeAddress;  // 가게 주소
    private String storeTelephone; // 가게 전화번호
    private String storeIntroduce; // 가게 소개
    private String businessTime;  // 영업시간
    private boolean isVisible;    // 노출 여부
    private BigDecimal reviewAvg; // 평균 리뷰 평점
    private int reviewCount;      // 리뷰 수
    private String categoryName;  // 카테고리 이름
    private UUID ownerId;         // 가게 주인 (Member)의 ID

    public static StoreResponseDto fromEntity(Store store) {
        return StoreResponseDto.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .storeAddress(store.getStoreAddress())
                .storeTelephone(store.getStoreTelephone())
                .storeIntroduce(store.getStoreIntroduce())
                .businessTime(store.getBusinessTime())
                .isVisible(store.isVisible())
                .reviewAvg(store.getReviewAvg() != null ? store.getReviewAvg() : BigDecimal.ZERO)
                .reviewCount(store.getReviewCount())
                .categoryName(store.getCategory() != null ? store.getCategory().getCategoryName() : null)
                .ownerId(store.getMember() != null ? store.getMember().getMemberId() : null)
                .build();
    }
}
