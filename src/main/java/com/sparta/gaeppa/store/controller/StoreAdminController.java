package com.sparta.gaeppa.store.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.service.StoreService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/admin/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreAdminController {

    private final StoreService storeService;

    // 1. 가게 전체를 조회하는 일 [ROLE : MANAGER, MASTER] -> StoreAdminController
    @GetMapping
    public ResponseEntity<ApiResult<List<Store>>> getAllStores() {
        List<Store> stores = storeService.getAllStores();
        return new ResponseEntity<>(success(stores), HttpStatus.OK);
    }

    // 2. 가게 삭제 API [ROLE : MANAGER, MASTER]
    @DeleteMapping("/{storeId}")
    public ResponseEntity<ApiResult<String>> deleteStore(@PathVariable UUID storeId) {
        storeService.deleteStore(storeId);
        return new ResponseEntity<>(success("Store가 정상 삭제 되었습니다."), HttpStatus.OK);
    }

}
