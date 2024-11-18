package com.sparta.gaeppa.product.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryPutRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryResponseDto;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import com.sparta.gaeppa.product.repository.ProductOptionCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductOptionRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOptionCategoryService {

    private final ProductRepository productRepository;
    private final ProductOptionCategoryRepository productOptionCategoryRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public ProductOptionCategoryResponseDto createProductOptionCategory(ProductOptionCategoryRequestDto requestDto,
                                                                        CustomUserDetails userDetails) {

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ServiceException(
                        ExceptionStatus.PRODUCT_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !product.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        ProductOptionCategory productOptionCategory = requestDto.toEntity();
        productOptionCategory.setProduct(product);

        return ProductOptionCategoryResponseDto.from(productOptionCategoryRepository.save(productOptionCategory));
    }

    @Transactional
    public void updateProductOptionCategory(UUID optionCategoryId, ProductOptionCategoryPutRequestDto requestDto,
                                            CustomUserDetails userDetails) {

        ProductOptionCategory productOptionCategory = productOptionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new ServiceException(
                        ExceptionStatus.PRODUCT_OPTION_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !productOptionCategory.getProduct().getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        productOptionCategory.update(requestDto.getProductOptionCategoryName(), requestDto.getMaxLimits());
    }

    @Transactional
    public void deleteProductOptionCategory(UUID optionCategoryId, CustomUserDetails userDetails) {

        ProductOptionCategory productOptionCategory = productOptionCategoryRepository.findById(optionCategoryId)
                .orElseThrow(() -> new ServiceException(
                        ExceptionStatus.PRODUCT_OPTION_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER
                && userDetails.getMemberRole() != MemberRole.MANAGER
                && !productOptionCategory.getProduct().getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        if (productOptionRepository.existsByProductOptionCategory(productOptionCategory)) {
            throw new ServiceException(ExceptionStatus.PRODUCT_OPTION_CATEGORY_HAS_OPTIONS);
        }

        productOptionCategory.delete(userDetails.getUsername());
    }
}
