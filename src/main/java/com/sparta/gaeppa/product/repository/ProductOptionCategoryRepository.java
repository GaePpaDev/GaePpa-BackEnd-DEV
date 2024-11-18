package com.sparta.gaeppa.product.repository;

import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionCategoryRepository extends JpaRepository<ProductOptionCategory, UUID> {
    List<ProductOptionCategory> findAllByProductId(UUID productId);
}
