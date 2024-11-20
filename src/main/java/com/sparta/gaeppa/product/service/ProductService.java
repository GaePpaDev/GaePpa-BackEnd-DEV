package com.sparta.gaeppa.product.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.product.ProductRequestDto;
import com.sparta.gaeppa.product.dto.product.ProductResponseDto;
import com.sparta.gaeppa.product.dto.product.StoreProductListResponseDto;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.ProductCategory;
import com.sparta.gaeppa.product.repository.ProductCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public StoreProductListResponseDto getAllProductsByStoreId(UUID storeId) {

        List<ProductCategory> productCategoryList = productCategoryRepository.findAllByStore_StoreId(storeId);

        if (productCategoryList.isEmpty()) {
            throw new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_NOT_FOUND);
        }

        int totalAmount = productRepository.countByProductCategory_Store_StoreId(storeId);

        return new StoreProductListResponseDto(totalAmount, productCategoryList);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto, CustomUserDetails userDetails) {

        ProductCategory productCategory = productCategoryRepository.findById(requestDto.getProductCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !productCategory.getCreatedBy().equals(userDetails.getUsername())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        Product newProduct = requestDto.toEntity();
        newProduct.setProductCategory(productCategory);

        return ProductResponseDto.from(productRepository.save(newProduct));
    }

    @Transactional
    public void updateProduct(UUID productId, ProductRequestDto requestDto, CustomUserDetails userDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_NOT_FOUND));

        ProductCategory productCategory = productCategoryRepository.findById(requestDto.getProductCategoryId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_CATEGORY_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER && (
                !product.getCreatedBy().equals(userDetails.getUsername()) || !productCategory.getCreatedBy()
                        .equals(userDetails.getUsername()))) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        product.setProductCategory(productCategory);
        product.update(requestDto.getProductName(), requestDto.getProductDescription(), requestDto.getProductPrice(),
                requestDto.isHideStatus());
    }

    @Transactional
    public void deleteProduct(UUID productId, CustomUserDetails userDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PRODUCT_NOT_FOUND));

        if (userDetails.getMemberRole() != MemberRole.MASTER && userDetails.getMemberRole() != MemberRole.MANAGER
                && !product.getCreatedBy().equals(userDetails.getMemberId())) {
            throw new ServiceException(ExceptionStatus.UNAUTHORIZED);
        }

        product.delete(userDetails.getMemberId());
    }

    @Transactional
    public Page<ProductResponseDto> searchProducts(String productName, String categoryName, String optionName,
                                                   String optionCategoryName, String storeCategoryName,
                                                   String sortBy,
                                                   String sortDirection,
                                                   int page,
                                                   int size) {

        if (size != 10 && size != 30 && size != 50) {
            throw new ServiceException(ExceptionStatus.INVALID_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        // Querydsl 동적 검색 수행
        Page<Product> products = productRepository.searchProducts(productName, categoryName, optionName,
                optionCategoryName, storeCategoryName, pageable, sortBy, sortDirection);

        // DTO 변환
        List<ProductResponseDto> responseDtoList = products.stream()
                .map(ProductResponseDto::from)
                .toList();

        return new PageImpl<>(responseDtoList, pageable, products.getTotalElements());
    }
}
