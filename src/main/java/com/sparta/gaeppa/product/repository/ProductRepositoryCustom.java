package com.sparta.gaeppa.product.repository;

import com.sparta.gaeppa.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchProducts(String productName, String categoryName, String optionName,
                                 String optionCategoryName, String storeCategoryName, Pageable pageable,
                                 String sortBy, String sortDirection);
}