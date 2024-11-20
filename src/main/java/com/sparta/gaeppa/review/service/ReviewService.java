package com.sparta.gaeppa.review.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.order.entity.OrderStatus;
import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.order.repository.OrderRepository;
import com.sparta.gaeppa.review.dto.ReviewRequestDto;
import com.sparta.gaeppa.review.dto.ReviewResponseDto;
import com.sparta.gaeppa.review.entity.Review;
import com.sparta.gaeppa.review.repository.ReviewRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.repository.StoreRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;


    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getAllReviewsByStoreId(UUID storeId) {

        List<Review> reviewList = reviewRepository.getAllReviewsByStoreId(storeId);

        return Optional.of(reviewList)
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().map(ReviewResponseDto::from).toList())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.REVIEW_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public ReviewResponseDto getReviewByOrderId(UUID orderId) {

        return Optional.ofNullable(reviewRepository.findByOrder_OrderIdAndDeletedAtIsNull(orderId))
                .map(ReviewResponseDto::from)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.REVIEW_NOT_FOUND));
    }

    @Transactional
    public ReviewResponseDto createReview(ReviewRequestDto requestDto) {

        Orders order = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.ORDER_NOT_FOUND));

        reviewAvailableOrder(order);    // 리뷰 작성 가능 여부 확인

        Review newReview = requestDto.toEntity(order);
        reviewRepository.save(newReview);

        Store store = storeRepository.findById(order.getStore().getStoreId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.STORE_NOT_FOUND));
        store.updateReviewAvg(requestDto.getReviewScore());

        return ReviewResponseDto.from(newReview);
    }

    private void reviewAvailableOrder(Orders order) {

        if (reviewRepository.existsReviewByOrder_orderId(order.getOrderId())) {
            throw new ServiceException(ExceptionStatus.REVIEW_ALREADY_EXISTS);
        }

        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(order.getCreatedAt(), now);

        // 주문 상태 "COMPLETED" 만 리뷰 작성 가능 / 주문 완료 후 60분 이후 부터 리뷰 작성 가능
        if (order.getOrderStatus() != OrderStatus.COMPLETED || duration.toMinutes() < 60) {
            throw new ServiceException(ExceptionStatus.REVIEW_NOT_AVAILABLE);
        }

    }

    @Transactional
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_CUSTOMER')")
    public void deleteReview(UUID reviewId, CustomUserDetails userDetails) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.REVIEW_NOT_FOUND));

        if(userDetails.getMemberRole() == MemberRole.CUSTOMER) {
            if(userDetails.getMemberId() != review.getOrder().getMember().getMemberId()) {
                throw new ServiceException(ExceptionStatus.ACCESS_DENIED);
            }
        }

        review.delete(userDetails.getMemberId());

        Store store = storeRepository.findById(review.getOrder().getStore().getStoreId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.STORE_NOT_FOUND));
        store.rollbackReviewAvg(review.getReviewScore());
    }
}
