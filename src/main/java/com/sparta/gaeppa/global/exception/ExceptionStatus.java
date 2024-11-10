package com.sparta.gaeppa.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {

    //Products
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "Product not found"), PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT,
            "P002", "Product already exists"), PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "P003",
            "Product option not found"), PRODUCT_OPTION_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "P004",
            "Product option category not found"), PRODUCT_OPTION_CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "P005",
            "Product option category already exists"),

    //ProductCategory
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "PC001", "상품 카테고리가 존제하지 않습니다."),
    PRODUCT_CATEGORY_HAS_PRODUCTS(HttpStatus.BAD_REQUEST, "PC002", "상품 카테고리에 상품이 존재합니다."),

    //Users

    //Orders

    // JWT
    INVALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "j001", "유효하지 않은 리프레시 토큰입니다."),

    // ROLE
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "r001", "권한이 없습니다."), //Common

    //Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "s001", "서버 에러입니다.");

    private final int status;
    private final String customCode;
    private final String message;
    private final String err;

    ExceptionStatus(HttpStatus httpStatus, String customCode, String message) {
        this.status = httpStatus.value();
        this.customCode = customCode;
        this.message = message;
        this.err = httpStatus.getReasonPhrase();
    }
}
