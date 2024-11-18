package com.sparta.gaeppa.product.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionListResponseDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionRequestDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionResponseDto;
import com.sparta.gaeppa.product.entity.ProductOption;
import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import com.sparta.gaeppa.product.repository.ProductOptionCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductOptionRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionCategoryRepository productOptionCategoryRepository;
    private final ProductOptionRepository productOptionRepository;

    @Transactional
    public ProductOptionListResponseDto getProductOptionsByProductId(UUID productId) {

        List<ProductOptionCategory> productOptionCategoryList = productOptionCategoryRepository.findAllByProductId(
                productId);

        return ProductOptionListResponseDto.from(productOptionCategoryList);
    }

    @Transactional
    public ProductOptionResponseDto createProductOption(ProductOptionRequestDto requestDto) {

        ProductOptionCategory productOptionCategory = productOptionCategoryRepository.findById(
                        requestDto.getProductOptionCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_OPTION_CATEGORY_NOT_FOUND));

        ProductOption productOption = requestDto.toEntity();

        productOption.setProductOptionCategory(productOptionCategory);

        return ProductOptionResponseDto.from(productOptionRepository.save(productOption));
    }

    @Transactional
    public void updateProductOption(UUID optionId, ProductOptionRequestDto requestDto) {

        ProductOption productOption = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_OPTION_NOT_FOUND));

        ProductOptionCategory productOptionCategory = productOptionCategoryRepository.findById(
                        requestDto.getProductOptionCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_OPTION_CATEGORY_NOT_FOUND));

        productOption.setProductOptionCategory(productOptionCategory);

        productOption.update(requestDto);
    }

    @Transactional
    public void deleteProductOption(UUID optionId, CustomUserDetails userDetails) {

        ProductOption productOption = productOptionRepository.findById(optionId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_OPTION_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !productOption.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        productOption.delete(userDetails.getUsername());
    }
}
