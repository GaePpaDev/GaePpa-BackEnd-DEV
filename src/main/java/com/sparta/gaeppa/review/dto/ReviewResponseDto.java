package com.sparta.gaeppa.review.dto;

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
public class ReviewResponseDto {

    private UUID memberId;
    private String userName;
    private UUID orderId;
    private String createdAt;
    private String reviewContent;
    private int reviewScore;

    public static ReviewResponseDto from(Review review) {
        return ReviewResponseDto.builder()
//                .userName(review.getCreatedBy())
                .orderId(review.getOrder().getOrderId())
                .createdAt(review.getCreatedAt().toString())
                .reviewContent(review.getReviewContent())
                .reviewScore(review.getReviewScore())
                .build();
    }

}
