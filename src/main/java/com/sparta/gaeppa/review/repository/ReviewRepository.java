package com.sparta.gaeppa.review.repository;

import com.sparta.gaeppa.review.entity.Review;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsReviewByOrder_orderId(UUID orderId);

    @Query(value = "SELECT r FROM Review r JOIN Orders od on r.order.orderId = od.orderId WHERE r.deletedAt IS NULL")
    List<Review> getAllReviewsByStoreId(UUID storeId);

    Review findByOrder_OrderIdAndDeletedAtIsNull(UUID orderId);
}
