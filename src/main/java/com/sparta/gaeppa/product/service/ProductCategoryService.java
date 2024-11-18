package com.sparta.gaeppa.product.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryResponseDto;
import com.sparta.gaeppa.product.entity.ProductCategory;
import com.sparta.gaeppa.product.repository.ProductCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ProductCategoryResponseDto createProductCategory(ProductCategoryRequestDto requestDto,
                                                            CustomUserDetails userDetails) {

        Store store = storeRepository.getStoreByMember_MemberId(userDetails.getMemberId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.STORE_NOT_FOUND));

        ProductCategory newProductCategory = requestDto.toEntity();
        newProductCategory.setStore(store);

        ProductCategory savedProductCategory = productCategoryRepository.save(newProductCategory);

        return ProductCategoryResponseDto.from(savedProductCategory);
    }

    @Transactional
    public void updateProductCategory(UUID productCategoryId, ProductCategoryRequestDto requestDto,
                                      CustomUserDetails userDetails) {

        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER &&
                !productCategory.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        productCategory.updateName(requestDto.getCategoryName());
    }

    @Transactional
    public void deleteProductCategory(UUID productCategoryId, CustomUserDetails userDetails) {
        ProductCategory productCategory = productCategoryRepository.findById(productCategoryId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !productCategory.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        if (productRepository.existsByProductCategory(productCategory)) {
            throw new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_HAS_PRODUCTS);
        }

        productCategory.delete(userDetails.getUsername());
    }
}
