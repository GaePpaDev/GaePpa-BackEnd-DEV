package com.sparta.gaeppa.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.gaeppa.product.entity.Product;
import com.sparta.gaeppa.product.entity.QProduct;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchProducts(String productName, String categoryName, String optionName,
                                        String optionCategoryName, String storeCategoryName, Pageable pageable,
                                        String sortBy, String sortDirection) {
        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        // 조건 추가
        Optional.ofNullable(productName).ifPresent(name -> builder.and(product.name.containsIgnoreCase(name)));
        Optional.ofNullable(categoryName)
                .ifPresent(cat -> builder.and(product.productCategory.name.containsIgnoreCase(cat)));
        Optional.ofNullable(optionName)
                .ifPresent(opt -> builder.and(
                        product.productOptionCategories.any().productOptions.any().name.containsIgnoreCase(opt)));
        Optional.ofNullable(optionCategoryName).ifPresent(optCat -> builder.and(
                product.productOptionCategories.any().productOptions.any().productOptionCategory.name.containsIgnoreCase(
                        optCat)));
        Optional.ofNullable(storeCategoryName).ifPresent(
                storeCat -> builder.and(product.productCategory.store.storeName.containsIgnoreCase(storeCat)));

        // Query 실행
        List<Product> results = queryFactory.selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(resolveSort(sortBy, sortDirection, product)) // 정렬
                .fetch();

        // 결과를 Page로 변환
        long total = queryFactory.select()
                .from(product)
                .where(builder)
                .fetch().size();

        return new PageImpl<>(results, pageable, total);
    }

    // 정렬 조건 처리 메서드
    //TODO: 테스트 후 삭제
//    private Pageable resolveSort(Pageable pageable, String sortBy, String sortDirection) {
//        if (sortBy != null && sortDirection != null) {
//            Sort.Order order = "asc".equalsIgnoreCase(sortDirection)
//                    ? Sort.Order.asc(sortBy)
//                    : Sort.Order.desc(sortBy);
//            return Pageable.unpaged().withSort(Sort.by(order));
//        }
//        return pageable; // 기본 페이지 설정 유지
//    }

    private OrderSpecifier<?> resolveSort(String sortBy, String sortDirection, QProduct product) {
        if (sortBy == null || sortDirection == null) {
            return product.createdAt.desc(); // 기본 정렬은 createdAt 기준 내림차순
        }

        return switch (sortBy.toLowerCase()) {
            case "updatedat" ->
                    "asc".equalsIgnoreCase(sortDirection) ? product.updatedAt.asc() : product.updatedAt.desc();
            case "name" -> "asc".equalsIgnoreCase(sortDirection) ? product.name.asc() : product.name.desc();
            case "price" -> "asc".equalsIgnoreCase(sortDirection) ? product.price.asc() : product.price.desc();
            default -> "asc".equalsIgnoreCase(sortDirection) ? product.createdAt.asc() : product.createdAt.desc();
        };
    }
}
