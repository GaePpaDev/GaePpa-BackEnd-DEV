package com.sparta.gaeppa.product.repository;

import com.sparta.gaeppa.product.entity.ProductOption;
import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption, UUID> {
    boolean existsByProductOptionCategory(ProductOptionCategory productOptionCategory);
}
