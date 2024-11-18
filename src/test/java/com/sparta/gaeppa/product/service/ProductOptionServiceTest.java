package com.sparta.gaeppa.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionListResponseDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionRequestDto;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionResponseDto;
import com.sparta.gaeppa.product.entity.ProductOption;
import com.sparta.gaeppa.product.entity.ProductOptionCategory;
import com.sparta.gaeppa.product.repository.ProductOptionCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductOptionRepository;
import com.sparta.gaeppa.security.jwts.entity.AuthenticatedUserDto;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOptionServiceTest {

    @InjectMocks
    private ProductOptionService productOptionService;

    @Mock
    ProductOptionCategoryRepository productOptionCategoryRepository;

    @Mock
    ProductOptionRepository productOptionRepository;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto(UUID.randomUUID(), "example@example.com",
                "master",
                "password", MemberRole.MASTER, true);
        userDetails = new CustomUserDetails(authenticatedUserDto);
    }

    @Test
    @DisplayName("상품 옵션 가져오기 성공 테스트")
    void getProductOptionsByProductId() {
        //TODO: 테스트 개선 필요, 카테고리 내 옵션 리스트가 테스트 되고 있지 않음
        //given
        UUID productId = UUID.randomUUID();

        List<ProductOptionCategory> productOptionCategories = List.of(
                ProductOptionCategory.builder().name("category1").build(),
                ProductOptionCategory.builder().name("category2").build());

        given(productOptionCategoryRepository.findAllByProductId(productId)).willReturn(productOptionCategories);

        //when
        ProductOptionListResponseDto responseDto = productOptionService.getProductOptionsByProductId(productId);

        //then
        assertEquals(2, responseDto.getOptionCategoryAmount());
    }

    @Test
    @DisplayName("상품 옵션 생성 성공 테스트")
    void createProductOption() {
        ProductOptionRequestDto requestDto = ProductOptionRequestDto.builder()
                .productOptionCategoryId(UUID.randomUUID())
                .name("productOptionName")
                .price(1000)
                .build();

        ProductOptionCategory productOptionCategory = ProductOptionCategory.builder().build();

        given(productOptionCategoryRepository.findById(requestDto.getProductOptionCategoryId())).willReturn(
                java.util.Optional.of(productOptionCategory));
        given(productOptionRepository.save(any(ProductOption.class))).willReturn(requestDto.toEntity());

        //when
        ProductOptionResponseDto responseDto = productOptionService.createProductOption(requestDto);

        //then
        assertEquals(requestDto.getName(), responseDto.getOptionName());
        assertEquals(requestDto.getPrice(), responseDto.getOptionPrice());
    }

    @Test
    @DisplayName("상품 옵션 생성 실패 테스트 - 카테고리가 존재하지 않는 경우")
    void createProductOptionFail() {
        ProductOptionRequestDto requestDto = ProductOptionRequestDto.builder()
                .productOptionCategoryId(UUID.randomUUID())
                .name("productOptionName")
                .price(1000)
                .build();

        given(productOptionCategoryRepository.findById(requestDto.getProductOptionCategoryId())).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionService.createProductOption(requestDto));
        assertEquals("상품 옵션 카테고리가 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 옵션 수정 성공 테스트")
    void updateProductOption() {
        UUID optionId = UUID.randomUUID();
        ProductOptionRequestDto requestDto = ProductOptionRequestDto.builder()
                .productOptionCategoryId(UUID.randomUUID())
                .name("productOptionName")
                .price(1000)
                .build();

        ProductOption productOption = ProductOption.builder().build();

        given(productOptionRepository.findById(optionId)).willReturn(java.util.Optional.of(productOption));
        given(productOptionCategoryRepository.findById(requestDto.getProductOptionCategoryId())).willReturn(
                java.util.Optional.of(ProductOptionCategory.builder().build()));

        //when-then
        productOptionService.updateProductOption(optionId, requestDto);
    }

    @Test
    @DisplayName("상품 옵션 수정 실패 테스트 - 옵션이 존재하지 않는 경우")
    void updateProductOptionFail() {
        UUID optionId = UUID.randomUUID();
        ProductOptionRequestDto requestDto = ProductOptionRequestDto.builder()
                .productOptionCategoryId(UUID.randomUUID())
                .name("productOptionName")
                .price(1000)
                .build();

        given(productOptionRepository.findById(optionId)).willReturn(java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionService.updateProductOption(optionId, requestDto));
        assertEquals("상품 옵션이 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 옵션 수정 실패 테스트 - 카테고리가 존재하지 않는 경우")
    void updateProductOptionFail2() {
        UUID optionId = UUID.randomUUID();
        ProductOptionRequestDto requestDto = ProductOptionRequestDto.builder()
                .productOptionCategoryId(UUID.randomUUID())
                .name("productOptionName")
                .price(1000)
                .build();

        ProductOption productOption = ProductOption.builder().build();

        given(productOptionRepository.findById(optionId)).willReturn(java.util.Optional.of(productOption));
        given(productOptionCategoryRepository.findById(requestDto.getProductOptionCategoryId())).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionService.updateProductOption(optionId, requestDto));
        assertEquals("상품 옵션 카테고리가 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 옵션 삭제 성공 테스트")
    void deleteProductOption() {
        UUID optionId = UUID.randomUUID();

        ProductOption productOption = ProductOption.builder().build();

        given(productOptionRepository.findById(optionId)).willReturn(java.util.Optional.of(productOption));

        //when-then
        productOptionService.deleteProductOption(optionId, userDetails);
    }

    @Test
    @DisplayName("상품 옵션 삭제 실패 테스트 - 옵션이 존재하지 않는 경우")
    void deleteProductOptionFail() {
        UUID optionId = UUID.randomUUID();

        given(productOptionRepository.findById(optionId)).willReturn(java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productOptionService.deleteProductOption(optionId,
                        userDetails));
        assertEquals("상품 옵션이 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }
}