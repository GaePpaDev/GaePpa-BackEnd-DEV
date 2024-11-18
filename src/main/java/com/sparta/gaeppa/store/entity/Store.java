package com.sparta.gaeppa.store.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.members.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_id")
    private UUID storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_category_id", nullable = false)
    private StoreCategory category;

    @Column
    private String storeName;

    @Column(nullable = false)
    private String storeAddress;

    @Column(nullable = false)
    private String storeTelephone;

    @Column(nullable = true)
    private String storeIntroduce;

    private String businessTime;

    @Column(nullable = false)
    private boolean isVisible = true;

    private BigDecimal reviewAvg;

    private int reviewCount = 0;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favoritedBy = new ArrayList<>();


    public Store(UUID storeId, Member member, StoreCategory category, String storeName, String storeAddress,
                 String storeTelephone, String storeIntroduce, String businessTime, boolean isVisible,
                 BigDecimal reviewAvg,
                 int reviewCount) {
        this.storeId = storeId;
        this.member = member;
        this.category = category;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeTelephone = storeTelephone;
        this.storeIntroduce = storeIntroduce;
        this.businessTime = businessTime;
        this.isVisible = isVisible;
        this.reviewAvg = reviewAvg;
        this.reviewCount = reviewCount;
    }

    @Builder
    private Store(Member member, StoreCategory category, String storeName, String storeAddress,
                  String storeTelephone, String storeIntroduce, String businessTime,
                  boolean isVisible, BigDecimal reviewAvg, int reviewCount) {
        this.member = member;
        this.category = category;
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeTelephone = storeTelephone;
        this.storeIntroduce = storeIntroduce;
        this.businessTime = businessTime;
        this.isVisible = isVisible;
        this.reviewAvg = reviewAvg;
        this.reviewCount = reviewCount;
    }

    public void updateStore(String storeName, String storeAddress, String storeTelephone, String storeIntroduce,
                            String businessTime, StoreCategory category) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
        this.storeTelephone = storeTelephone;
        this.storeIntroduce = storeIntroduce;
        this.businessTime = businessTime;
        this.category = category;
    }

    // isVisible 값을 반전시키는 메서드
    public void toggleVisibility() {
        this.isVisible = !this.isVisible;
    }

    // Review 추가시 업데이트시키는 메서드
    public void updateReviewAvg(int newReviewScore) {

        this.reviewCount++;
        this.reviewAvg = this.reviewAvg.multiply(BigDecimal.valueOf(reviewCount - 1))
                .add(BigDecimal.valueOf(newReviewScore))
                .divide(BigDecimal.valueOf(reviewCount), 2, RoundingMode.HALF_UP); // 소수점 둘째 자리에서 반올림
    }
}
