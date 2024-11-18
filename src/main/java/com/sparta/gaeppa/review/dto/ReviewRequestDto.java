package com.sparta.gaeppa.review.dto;

import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.review.entity.Review;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReviewRequestDto {

    private UUID memberId;
    private UUID orderId;
    private String reviewContent;
    private int reviewScore;

    public Review toEntity(Orders order) {
        return Review.builder()
                .order(order)
                .reviewContent(reviewContent)
                .reviewScore(reviewScore)
                .build();
    }

    public void setMemberIdByUserDetail(UUID memberId) {
        this.memberId = memberId;
    }
}
