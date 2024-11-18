package com.sparta.gaeppa.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_store_category")
public class StoreCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "store_category_id")
    private UUID storeCategoryId;

    @Column(name = "category_name", nullable = false, length = 50, unique = true)
    private String categoryName;

    @Builder
    private StoreCategory(UUID storeCategoryId, String categoryName) {
        this.storeCategoryId = storeCategoryId;
        this.categoryName = categoryName;
    }
}
