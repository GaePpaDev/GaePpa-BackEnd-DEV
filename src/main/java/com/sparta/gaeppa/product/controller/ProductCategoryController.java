package com.sparta.gaeppa.product.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryResponseDto;
import com.sparta.gaeppa.product.service.ProductCategoryService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<ProductCategoryResponseDto>> createProductCategory(
            @RequestBody ProductCategoryRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProductCategoryResponseDto productCategoryResponseDto = productCategoryService.createProductCategory(
                requestDto, userDetails);

        return new ResponseEntity<>(success(productCategoryResponseDto), HttpStatus.CREATED);
    }


    @PutMapping("/{productCategoryId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> updateProductCategory(@PathVariable UUID productCategoryId,
                                                                   @RequestBody ProductCategoryRequestDto requestDto,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        productCategoryService.updateProductCategory(productCategoryId, requestDto, userDetails);

        return new ResponseEntity<>(success("Product Category Updated"), HttpStatus.OK);
    }


    @DeleteMapping("/{productCategoryId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> deleteProductCategory(@PathVariable UUID productCategoryId,
                                                                   @AuthenticationPrincipal
                                                                   CustomUserDetails userDetails) {

        productCategoryService.deleteProductCategory(productCategoryId, userDetails);

        return new ResponseEntity<>(success("Product Category Deleted"), HttpStatus.NO_CONTENT);
    }
}
