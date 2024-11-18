package com.sparta.gaeppa.product.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionListResponseDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionRequestDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionResponseDto;
import com.sparta.gaeppa.product.service.ProductOptionService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/product-categories/products")
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionService productOptionService;

    @GetMapping("/{productId}/options")
    public ResponseEntity<ApiResult<ProductOptionListResponseDto>> getProductOptions(@PathVariable UUID productId) {

        ProductOptionListResponseDto responseDto = productOptionService.getProductOptionsByProductId(productId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping("/options")
    public ResponseEntity<ApiResult<ProductOptionResponseDto>> createProductOption(
            @RequestBody ProductOptionRequestDto requestDto) {

        ProductOptionResponseDto productOptionResponseDto = productOptionService.createProductOption(requestDto);

        return new ResponseEntity<>(success(productOptionResponseDto), HttpStatus.CREATED);
    }

    @PutMapping("/options/{optionId}")
    public ResponseEntity<ApiResult<String>> updateProductOption(@PathVariable UUID optionId,
                                                                 @RequestBody ProductOptionRequestDto requestDto) {

        productOptionService.updateProductOption(optionId, requestDto);

        return new ResponseEntity<>(success("Update Product Option Success"), HttpStatus.OK);
    }

    @DeleteMapping("/options/{optionId}")
    public ResponseEntity<ApiResult<String>> deleteProductOption(@PathVariable UUID optionId,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        productOptionService.deleteProductOption(optionId, userDetails);

        return new ResponseEntity<>(success("Delete Product Option Success"), HttpStatus.NO_CONTENT);
    }
}
