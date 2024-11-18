package com.sparta.gaeppa.ai.dto;

import com.sparta.gaeppa.ai.entity.AIPrompt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class SystemPromptResponseDto {

    private String prompt;

    public static SystemPromptResponseDto from(AIPrompt aiPrompt) {
        return SystemPromptResponseDto.builder()
                .prompt(aiPrompt.getPrompt())
                .build();
    }
}
