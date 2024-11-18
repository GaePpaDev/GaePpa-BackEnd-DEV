package com.sparta.gaeppa.image.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.global.storage.StorageService;
import com.sparta.gaeppa.image.dto.ImageResponseDto;
import com.sparta.gaeppa.image.dto.ResourceDto;
import com.sparta.gaeppa.image.entity.ReviewImage;
import com.sparta.gaeppa.image.mime.MimeType;
import com.sparta.gaeppa.image.repository.ReviewImageRepository;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.review.entity.Review;
import com.sparta.gaeppa.review.repository.ReviewRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.nio.file.Path;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReviewImageService {

    private final StorageService storageService;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;

    private static final Path REVIEW_IMAGE_PATH = Path.of("review-images");

    public ReviewImageService(StorageService storageService, ReviewImageRepository reviewImageRepository,
                              ReviewRepository reviewRepository) {
        this.storageService = storageService;
        this.reviewImageRepository = reviewImageRepository;
        this.reviewRepository = reviewRepository;
        this.storageService.createDirectory(REVIEW_IMAGE_PATH);
    }

    public ImageResponseDto uploadReviewImage(String productId, MultipartFile file) {

        Review review = reviewRepository.findById(UUID.fromString(productId))
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_NOT_FOUND));

        String fileName = productId + "-" + UUID.randomUUID() + "-" + System.currentTimeMillis();

        storageService.store(file, REVIEW_IMAGE_PATH, fileName);

        ReviewImage reviewImage = ReviewImage.builder().review(review).originalName(file.getOriginalFilename())
                .fileName(fileName).mimeType(MimeType.valueOf(file.getContentType())).build();

        return ImageResponseDto.from(reviewImageRepository.save(reviewImage));
    }

    public ResourceDto getReviewImage(UUID imageId) {
        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_IMAGE_NOT_FOUND));

        Resource resource = storageService.loadAsResource(reviewImage.getFileName());
        return ResourceDto.builder().resource(resource).mimeType(reviewImage.getMimeType()).build();
    }

    public void deleteReviewImage(UUID imageId, CustomUserDetails userDetails) {

        ReviewImage reviewImage = reviewImageRepository.findById(imageId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_IMAGE_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MANAGER && userDetails.getMemberRole() != MemberRole.OWNER
                && !reviewImage.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        reviewImage.delete(userDetails.getUsername());
    }
}
