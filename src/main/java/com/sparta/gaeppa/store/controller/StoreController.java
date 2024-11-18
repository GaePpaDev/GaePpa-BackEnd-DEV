package com.sparta.gaeppa.store.controller;


import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.base.AuditorAwareImpl;
import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.store.dto.StoreCreateRequestDto;
import com.sparta.gaeppa.store.dto.StoreResponseDto;
import com.sparta.gaeppa.store.dto.StoreUpdateRequestDto;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.service.StoreService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/v1/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final AuditorAwareImpl auditorAware;

    /**
     * 1. 가게를 새롭게 등록하는 일 [ROLE : OWNER] POST
     * - 자신이 자신의 가게를 등록
     * @param requestDto storeCategoryName -> StoreCategory
     * storeName, storeAddress, storeTelephone, storeIntroduce, businessTime -> StoreCreateRequestDto 입력인자
     * reviewAvg, reviewCount -> 'Default = 0' 으로 설정
     * @return
     */
    @PostMapping
    public ResponseEntity<ApiResult<StoreResponseDto>> createStore(@Valid @RequestBody StoreCreateRequestDto requestDto) {

        Store store = storeService.createStore(requestDto);

        return new ResponseEntity<>(success(StoreResponseDto.fromEntity(store)), HttpStatus.CREATED);
    }

    /**
     * 2. 나의 가게에 대한 정보를 조회 [ROLE : OWNER] GET
     */
    @GetMapping("/{storeId}/{memberId}")
    public ResponseEntity<ApiResult<StoreResponseDto>> getMyStore(@PathVariable UUID storeId,
                                                                @PathVariable UUID memberId) {
        Store store = storeService.getMyStore(storeId, memberId);

        return new ResponseEntity<>(success(StoreResponseDto.fromEntity(store)), HttpStatus.OK);
    }

    /**
     * 3. 가게 정보를 수정하는 일  [ROLE : OWNER] PUT
     * storeName, storeAddress, storeTelephone, storeIntroduce, businessTime -> StoreUpdateRequestDto 입력인자
     */
    @PutMapping("/{storeId}/{memberId}")
    public ResponseEntity<ApiResult<StoreResponseDto>> updateStore(@Valid @RequestBody StoreUpdateRequestDto requestDto,
                                                                   @PathVariable UUID storeId,
                                                                   @PathVariable UUID memberId) {
        Store store = storeService.updateStore(requestDto, storeId, memberId);

        return new ResponseEntity<>(success(StoreResponseDto.fromEntity(store)), HttpStatus.CREATED);
    }

    /**
     * 4. 가게를 비활성화 or 재활성화(검색이 불가하도록) 하는 일 [ROLE : OWNER]
     * 인자를 받지 않고,
     * (1) 가게가 활성화 되어 있다 -> 비활성화 하는 작업을 진행
     * (2) 가게가 비활성화 되어 있다 -> 재활성화 하는 작업을 진행
     */
    @PostMapping("/{storeId}/{memberId}")
    public ResponseEntity<ApiResult<String>> deActivateStore(@PathVariable UUID storeId, @PathVariable UUID memberId) {

        Store store = storeService.getMyStore(storeId, memberId);
        store.toggleVisibility(); // 항상 상태를 반전

        String message = store.isVisible()
                ? "스토어 검색을 활성화 처리 했습니다."
                : "스토어 검색을 비활성화 처리 했습니다.";

        return new ResponseEntity<>(success(message), HttpStatus.CREATED);
    }

    /**
     * 5. 고객들이 가게에 대한 정보를 조회 [ROLE : CUSTOMER] GET
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<ApiResult<StoreResponseDto>> getStore(@PathVariable UUID storeId) {

        Store store = storeService.getStore(storeId);

        return new ResponseEntity<>(success(StoreResponseDto.fromEntity(store)), HttpStatus.OK);
    }
    // 1. 가게를 새롭게 등록하는 일 [ROLE : OWNER] POST
    // 2. 나의 가게에 대한 정보를 조회 [ROLE : OWNER] GET
    // 3. 가게 정보를 수정하는 일  [ROLE : OWNER] PUT
    // 4. 가게를 비활성화(검색이 불가하도록), 재활성화 하는 일 [ROLE : OWNER]

    // 가게의 리뷰가 달릴 때, reviewAvg, reviewCount 를 변경해주는 서비스 메서드가 필요함. -> 컨트롤러는 ReviewController 에서 관리

}