package com.sparta.gaeppa.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.product.dto.product.ProductRequestDto;
import com.sparta.gaeppa.product.dto.product.ProductResponseDto;
import com.sparta.gaeppa.product.dto.product.StoreProductListResponseDto;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.ProductCategory;
import com.sparta.gaeppa.product.repository.ProductCategoryRepository;
import com.sparta.gaeppa.product.repository.ProductRepository;
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
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

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
    @DisplayName("모든 상품 조회 성공 테스트")
    void getAllProductsByStoreId() {
        //given
        UUID storeId = UUID.randomUUID();

        List<ProductCategory> productCategories = List.of(
                ProductCategory.builder().name("category1").build(),
                ProductCategory.builder().name("category2").build());

        given(productCategoryRepository.findAllByStore_StoreId(storeId)).willReturn(productCategories);

        //when
        StoreProductListResponseDto storeProductListResponseDto = productService.getAllProductsByStoreId(storeId);

        //then
        assertEquals(0, storeProductListResponseDto.getTotalAmount());
        assertEquals(2, storeProductListResponseDto.getDetails().size());
    }

    @Test
    @DisplayName("상품 생성 성공 테스트")
    void createProduct() {
        //given
        UUID productCategoryId = UUID.randomUUID();

        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .productName("product1")
                .productPrice(1000)
                .productDescription("description")
                .productCategoryId(productCategoryId)
                .build();

        Product product = productRequestDto.toEntity();

        given(productCategoryRepository.findById(productCategoryId)).willReturn(
                java.util.Optional.of(ProductCategory.builder().name("category1").build()));
        given(productRepository.save(any(Product.class))).willReturn(product);

        //when
        ProductResponseDto responseDto = productService.createProduct(productRequestDto, userDetails);

        //then
        assertEquals(product.getName(), responseDto.getProductName());
        assertEquals(product.getPrice(), responseDto.getProductPrice());
        assertEquals(product.getDescription(), responseDto.getProductDescription());
        assertFalse(product.isHideStatus());
    }

    @Test
    @DisplayName("상품 생성 실패 테스트 - 존재하지 않는 상품 카테고리")
    void createProductFail() {
        //given
        UUID productCategoryId = UUID.randomUUID();

        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .productName("product1")
                .productPrice(1000)
                .productDescription("description")
                .productCategoryId(productCategoryId)
                .build();

        given(productCategoryRepository.findById(productCategoryId)).willReturn(java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productService.createProduct(productRequestDto, userDetails));
        assertEquals("상품 카테고리가 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 수정 성공 테스트")
    void updateProduct() {
        //given
        UUID productId = UUID.randomUUID();
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .productName("product2")
                .productPrice(1000)
                .productDescription("description2")
                .productCategoryId(UUID.randomUUID())
                .build();

        Product product = productRequestDto.toEntity();

        given(productRepository.findById(productId)).willReturn(java.util.Optional.of(product));
        given(productCategoryRepository.findById(productRequestDto.getProductCategoryId())).willReturn(
                java.util.Optional.of(ProductCategory.builder().name("category2").build()));

        //when-then
        productService.updateProduct(productId, productRequestDto, userDetails);
    }

    @Test
    @DisplayName("상품 수정 실패 테스트 - 존재하지 않는 상품")
    void updateProductFail() {
        //given
        UUID productId = UUID.randomUUID();
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .productName("product2")
                .productPrice(1000)
                .productDescription("description2")
                .productCategoryId(UUID.randomUUID())
                .build();

        given(productRepository.findById(productId)).willReturn(java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productService.updateProduct(productId, productRequestDto, userDetails));
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }

    @Test
    @DisplayName("상품 수정 실패 테스트 - 존재하지 않는 상품 카테고리")
    void updateProductFail2() {
        //given
        UUID productId = UUID.randomUUID();
        ProductRequestDto productRequestDto = ProductRequestDto.builder()
                .productName("product2")
                .productPrice(1000)
                .productDescription("description2")
                .productCategoryId(UUID.randomUUID())
                .build();

        Product product = productRequestDto.toEntity();

        given(productRepository.findById(productId)).willReturn(java.util.Optional.of(product));
        given(productCategoryRepository.findById(productRequestDto.getProductCategoryId())).willReturn(
                java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class,
                () -> productService.updateProduct(productId, productRequestDto, userDetails));
        assertEquals("상품 카테고리가 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("상품 삭제 성공 테스트")
    void deleteProduct() {
        //given
        UUID productId = UUID.randomUUID();

        given(productRepository.findById(productId)).willReturn(java.util.Optional.of(Product.builder().build()));

        //when-then
        productService.deleteProduct(productId, userDetails);
    }

    @Test
    @DisplayName("상품 삭제 실패 테스트 - 존재하지 않는 상품")
    void deleteProductFail() {
        //given
        UUID productId = UUID.randomUUID();

        given(productRepository.findById(productId)).willReturn(java.util.Optional.empty());

        //when-then
        ServiceException exception = assertThrows(ServiceException.class, () -> productService.deleteProduct(productId,
                userDetails));
        assertEquals("상품이 존재하지 않습니다.", exception.getMessage());
        assertEquals(404, exception.getStatus());
    }
}