package com.sparta.gaeppa.product.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryPutRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productOptionCategory.ProductOptionCategoryResponseDto;
import com.sparta.gaeppa.product.service.ProductOptionCategoryService;
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
@RequestMapping("/v1/product-categories/products/option-categories")
@RequiredArgsConstructor
public class ProductOptionCategoryController {

    private final ProductOptionCategoryService productOptionCategoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<ProductOptionCategoryResponseDto>> createProductOptionCategory(
            @RequestBody ProductOptionCategoryRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProductOptionCategoryResponseDto productOptionCategoryResponseDto = productOptionCategoryService.createProductOptionCategory(
                requestDto, userDetails);

        return new ResponseEntity<>(success(productOptionCategoryResponseDto), HttpStatus.CREATED);
    }

    @PutMapping("/{optionCategoryId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> updateProductOptionCategory(@PathVariable UUID optionCategoryId,
                                                                         @RequestBody ProductOptionCategoryPutRequestDto requestDto,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        productOptionCategoryService.updateProductOptionCategory(optionCategoryId, requestDto, userDetails);

        return new ResponseEntity<>(success("Update Product Option Category Success"), HttpStatus.OK);
    }

    @DeleteMapping("/{optionCategoryId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> deleteProductOptionCategory(@PathVariable UUID optionCategoryId,
                                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {

        productOptionCategoryService.deleteProductOptionCategory(optionCategoryId, userDetails);

        return new ResponseEntity<>(success("Delete Product Option Category Success"), HttpStatus.NO_CONTENT);
    }
}
