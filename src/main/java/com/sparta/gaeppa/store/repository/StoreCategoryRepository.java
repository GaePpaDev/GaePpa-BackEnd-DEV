package com.sparta.gaeppa.store.repository;

import com.sparta.gaeppa.store.entity.StoreCategory;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, UUID> {
    Optional<StoreCategory> findByCategoryName(String categoryName);
}
