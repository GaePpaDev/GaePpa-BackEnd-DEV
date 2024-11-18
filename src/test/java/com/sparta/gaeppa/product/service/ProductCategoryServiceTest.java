package com.sparta.gaeppa.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryRequestDto;
import com.sparta.gaeppa.product.dto.productCategory.ProductCategoryResponseDto;
import com.sparta.gaeppa.product.entity.ProductCategory;
import com.sparta.gaeppa.product.repository.ProductCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
import com.sparta.gaeppa.security.jwts.entity.AuthenticatedUserDto;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.repository.StoreRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductCategoryServiceTest {

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductCategoryRepository productCategoryRepository;

    @Mock
    private ProductRepository productRepository;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(UUID.randomUUID(), "example@example.com",
                "master",
                "password", MemberRole.MASTER, true);
        userDetails = new CustomUserDetails(authenticatedUserDto);
    }

    @Test
    @DisplayName("상품 카테고리 생성 성공 테스트")
    void createProductCategory() {
        //given
        ProductCategoryRequestDto requestDto = ProductCategoryRequestDto.builder().categoryName("면류").build();

        ProductCategory categoryEntity = requestDto.toEntity();

        given(storeRepository.getStoreByMember_MemberId(any(UUID.class))).willReturn(
                Optional.ofNullable(Store.builder().build()));

        given(productCategoryRepository.save(any(ProductCategory.class))).willReturn(categoryEntity);

        //when
        ProductCategoryResponseDto responseDto = productCategoryService.createProductCategory(requestDto, userDetails);

        //then
        assertEquals(requestDto.getCategoryName(), responseDto.getName());
    }

    @Test
    @DisplayName("상품 카테고리 수정 성공 테스트")
    void updateProductCategory() {
        //given
        ProductCategoryRequestDto requestDto = ProductCategoryRequestDto.builder().categoryName("면류").build();

        given(productCategoryRepository.findById(any(UUID.class))).willReturn(
                java.util.Optional.of(requestDto.toEntity()));

        //when-then
        productCategoryService.updateProductCategory(UUID.randomUUID(), requestDto, userDetails);
    }

    @Test
    @DisplayName("카테고리 수정 실패 테스트 - 존재하지 않는 상품 카테고리")
    void updateProductCategoryFail() {
        //given
        ProductCategoryRequestDto requestDto = ProductCategoryRequestDto.builder().categoryName("면류").build();

        given(productCategoryRepository.findById(any(UUID.class))).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productCategoryService.updateProductCategory(UUID.randomUUID(), requestDto, userDetails));
        assertEquals("상품 카테고리가 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 카테고리 삭제 성공 테스트")
    void deleteProductCategory() {
        //given
        UUID categoryId = UUID.randomUUID();

        ProductCategory productCategory = ProductCategory.builder().name("name").build();

        given(productCategoryRepository.findById(any(UUID.class))).willReturn(
                java.util.Optional.of(productCategory));

        //when-then
        productCategoryService.deleteProductCategory(categoryId, userDetails);
    }

    @Test
    @DisplayName("카테고리 삭제 실패 테스트 - 존재하지 않는 상품 카테고리")
    void deleteProductCategoryFail() {
        //given
        UUID categoryId = UUID.randomUUID();

        given(productCategoryRepository.findById(any(UUID.class))).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productCategoryService.deleteProductCategory(categoryId, userDetails));
        assertEquals("상품 카테고리가 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 카테고리 삭제 실패 테스트 - 상품이 존재하는 상품 카테고리")
    void deleteProductCategoryFail2() {
        //given
        UUID categoryId = UUID.randomUUID();

        ProductCategory productCategory = ProductCategory.builder().name("name").build();

        given(productCategoryRepository.findById(any(UUID.class))).willReturn(
                java.util.Optional.of(productCategory));

        given(productRepository.existsByProductCategory(any(ProductCategory.class))).willReturn(true);

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productCategoryService.deleteProductCategory(categoryId, userDetails));
        assertEquals("상품 카테고리에 상품이 존재합니다.", exception.getMessage());
        assertEquals(400, exception.getStatus());
    }
}

