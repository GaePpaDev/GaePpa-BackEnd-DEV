package com.sparta.gaeppa.product.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.product.dto.ProductCategoryRequestDto;
import com.sparta.gaeppa.product.dto.ProductCategoryResponseDto;
import com.sparta.gaeppa.product.service.ProductCategoryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping()
    public ResponseEntity<ApiResult<ProductCategoryResponseDto>> createProductCategory(
            @RequestBody ProductCategoryRequestDto requestDto) {

        ProductCategoryResponseDto productCategoryResponseDto = productCategoryService.createProductCategory(
                requestDto);

        return new ResponseEntity<>(success(productCategoryResponseDto), HttpStatus.CREATED);
    }


    @PutMapping("/{productCategoryId}")
    public ResponseEntity<ApiResult<String>> updateProductCategory(@PathVariable UUID productCategoryId,
                                                                   @RequestBody ProductCategoryRequestDto requestDto) {

        productCategoryService.updateProductCategory(productCategoryId, requestDto);

        return new ResponseEntity<>(success("Product Category Updated"), HttpStatus.OK);
    }


    @DeleteMapping("/{productCategoryId}")
    public ResponseEntity<ApiResult<String>> deleteProductCategory(@PathVariable UUID productCategoryId) {

        productCategoryService.deleteProductCategory(productCategoryId);

        return new ResponseEntity<>(success("Product Category Deleted"), HttpStatus.OK);
    }
}