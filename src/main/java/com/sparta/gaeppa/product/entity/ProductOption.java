package com.sparta.gaeppa.product.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.product.dto.productOption.ProductOptionRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_product_options")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class ProductOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_option_id")
    private UUID id;

    @Column(name = "product_option_name", nullable = false)
    private String name;

    @Column(name = "product_option_price", nullable = false)
    private int price;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_category_id", nullable = false)
    private ProductOptionCategory productOptionCategory;

    @Builder
    private ProductOption(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void update(ProductOptionRequestDto requestDto) {
        this.name = requestDto.getName();
        this.price = requestDto.getPrice();
    }
}