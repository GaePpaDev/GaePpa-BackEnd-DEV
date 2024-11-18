package com.sparta.gaeppa.image.repository;


import com.sparta.gaeppa.image.entity.ReviewImage;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {
}
