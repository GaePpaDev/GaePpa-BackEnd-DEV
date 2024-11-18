package com.sparta.gaeppa.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AssistProductDescriptionResponseDto {
    private String productDescriptionRecommendation;
}
