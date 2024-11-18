package com.sparta.gaeppa.store.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.store.entity.StoreCategory;
import com.sparta.gaeppa.store.repository.StoreCategoryRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreCategoryService {
    private final StoreCategoryRepository storeCategoryRepository;

    /**
     * 카테고리를 생성
     * @param storeCategoryName 카테고리 이름
     * @return 생성된 StoreCategory
     */
    @Transactional
    public StoreCategory createStoreCategoryByName(String storeCategoryName) {

        // 카테고리 이름으로 조회
        StoreCategory newCategory = StoreCategory.builder()
                .categoryName(storeCategoryName)
                .build();
        return storeCategoryRepository.save(newCategory);
    }

    /**
     * 카테고리 조회
     * @param storeCategoryName 카테고리 이름
     * @return Optional<StoreCategory>
     */
    @Transactional(readOnly = true)
    public StoreCategory findStoreCategoryByName(String storeCategoryName) {
        // 카테고리 이름으로 조회
        return storeCategoryRepository.findByCategoryName(storeCategoryName)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.STORE_CATEGORY_NAME_NOT_FOUND));
    }

    /**
     * 카테고리가 존재하면 예외 처리, 없으면 생성
     * @param storeCategoryName 카테고리 이름
     * @return 생성된 StoreCategory
     */
    @Transactional
    public StoreCategory createIfNotExists(String storeCategoryName) {
        return storeCategoryRepository.findByCategoryName(storeCategoryName)
                .orElseGet(() -> createStoreCategoryByName(storeCategoryName));
    }
}
