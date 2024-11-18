package com.sparta.gaeppa.review.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.review.dto.ReviewRequestDto;
import com.sparta.gaeppa.review.dto.ReviewResponseDto;
import com.sparta.gaeppa.review.service.ReviewService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResult<List<ReviewResponseDto>>> getAllReviewsByStoreId(
            @RequestParam("storeId") UUID storeId) {

        List<ReviewResponseDto> responseDto = reviewService.getAllReviewsByStoreId(storeId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }


    @GetMapping("/orderId")
    public ResponseEntity<ApiResult<ReviewResponseDto>> getReviewByOrderId(@RequestParam("orderId") UUID orderId) {

        ReviewResponseDto responseDto = reviewService.getReviewByOrderId(orderId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResult<ReviewResponseDto>> createReview(@RequestBody ReviewRequestDto requestDto,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        requestDto.setMemberIdByUserDetail(userDetails.getMemberId());
        ReviewResponseDto responseDto = reviewService.createReview(requestDto);

        return new ResponseEntity<>(success(responseDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ApiResult<String>> deleteReview(@RequestParam UUID reviewId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {

        // 삭제 권한 manager 제한 필요
        reviewService.deleteReview(reviewId, userDetails);

        return new ResponseEntity<>(success("Review deleted Success"), HttpStatus.OK);
    }
}
