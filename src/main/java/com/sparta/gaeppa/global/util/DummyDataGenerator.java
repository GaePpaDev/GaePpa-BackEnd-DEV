//package com.sparta.gaeppa.global.util;
//
//import com.sparta.gaeppa.product.entity.Product;
//import com.sparta.gaeppa.product.entity.ProductCategory;
//import com.sparta.gaeppa.product.repository.ProductCategoryRepository;
//import com.sparta.gaeppa.product.repository.ProductRepository;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DummyDataGenerator implements CommandLineRunner {
//
//    @Autowired
//    private ProductCategoryRepository productCategoryRepository;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        // 더미 ProductCategory 생성 (음식 카테고리)
//        ProductCategory foodCategory1 = ProductCategory.builder()
//                .name("국물요리")
//                .storeId(UUID.randomUUID())
//                .build();
//
//        ProductCategory foodCategory2 = ProductCategory.builder()
//                .name("밥요리")
//                .storeId(UUID.randomUUID())
//                .build();
//
//        productCategoryRepository.save(foodCategory1);
//        productCategoryRepository.save(foodCategory2);
//
//        // 더미 Product 생성 1 (음식 1)
//        Product food1 = Product.builder()
//                .name("김치찌개") // 김치찌개
//                .description("김치찌개 맛있다")
//                .price(12000) // 가격 설정
//                .hideStatus(false) // 숨기지 않음
//                .category(foodCategory1) // 음식 카테고리 설정
//                .build();
//        productRepository.save(food1);
//
//        // 더미 Product 생성 2 (음식 2)
//        Product food2 = Product.builder()
//                .name("부대찌개") // 김치찌개
//                .description("부대찌개 맛있다.")
//                .price(12000) // 가격 설정
//                .hideStatus(false) // 숨기지 않음
//                .category(foodCategory1) // 음식 카테고리 설정
//                .build();
//
//        productRepository.save(food2);
//
//        // 더미 Product 생성 3 (음식 3)
//        Product food3 = Product.builder()
//                .name("볶음밥") // 김치찌개
//                .description("고기먹고는 역시 볶음밥")
//                .price(8000) // 가격 설정
//                .hideStatus(false) // 숨기지 않음
//                .category(foodCategory2) // 음식 카테고리 설정
//                .build();
//
//        productRepository.save(food3);
//
//        // 더미 Product 생성 4 (음식 4)
//        Product food4 = Product.builder()
//                .name("비빔밥") // 김치찌개
//                .description("막비벼 비벼비벼.")
//                .price(10000) // 가격 설정
//                .hideStatus(false) // 숨기지 않음
//                .category(foodCategory2) // 음식 카테고리 설정
//                .build();
//
//        productRepository.save(food4);
//
//        // 더미 Product 생성 5 (음식 5)
//        Product food5 = Product.builder()
//                .name("누룽지") // 김치찌개
//                .description("구수~ 한 누룽지")
//                .price(7000) // 가격 설정
//                .hideStatus(false) // 숨기지 않음
//                .category(foodCategory2) // 음식 카테고리 설정
//                .build();
//
//        productRepository.save(food5);
//
//        System.out.println("Dummy food data generated successfully.");
//    }
//}