package com.sparta.gaeppa.cart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Images {

    @Column(name = "img_name")
    private String imgName;
    @Column(name = "img_path")
    private String imgPath;
}
