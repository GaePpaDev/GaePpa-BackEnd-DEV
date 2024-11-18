package com.sparta.gaeppa.product.repository;

import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.ProductCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    boolean existsByProductCategory(ProductCategory category);

    int countByProductCategory_Store_StoreId(UUID storeId);
}
