package com.sparta.gaeppa.product.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.product.dto.product.ProductRequestDto;
import com.sparta.gaeppa.product.dto.product.ProductResponseDto;
import com.sparta.gaeppa.product.dto.product.StoreProductListResponseDto;
import com.sparta.gaeppa.product.service.ProductService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product-categories/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/search")
    public ResponseEntity<ApiResult<Page<ProductResponseDto>>> searchProducts(
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String optionName,
            @RequestParam(required = false) String optionCategoryName,
            @RequestParam(required = false) String storeCategoryName,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        Page<ProductResponseDto> responseDtoPage = productService.searchProducts(productName, categoryName, optionName,
                optionCategoryName, storeCategoryName, sortBy, sortDirection, page, size);

        return new ResponseEntity<>(success(responseDtoPage), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResult<StoreProductListResponseDto>> getProducts(@RequestParam("storeid") UUID storeId) {

        StoreProductListResponseDto responseDto = productService.getAllProductsByStoreId(storeId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<ProductResponseDto>> createProduct(@RequestBody ProductRequestDto requestDto,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        ProductResponseDto productResponseDto = productService.createProduct(requestDto, userDetails);

        return new ResponseEntity<>(success(productResponseDto), HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> updateProduct(@PathVariable UUID productId,
                                                           @RequestBody ProductRequestDto requestDto,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        productService.updateProduct(productId, requestDto, userDetails);

        return new ResponseEntity<>(success("Update Product Success"), HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ROLE_MASTER', 'ROLE_MANAGER', 'ROLE_OWNER')")
    public ResponseEntity<ApiResult<String>> deleteProduct(@PathVariable UUID productId, @AuthenticationPrincipal
    CustomUserDetails userDetails) {

        productService.deleteProduct(productId, userDetails);

        return new ResponseEntity<>(success("Delete Product Success"), HttpStatus.NO_CONTENT);
    }
}
