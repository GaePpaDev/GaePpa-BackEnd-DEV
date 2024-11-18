package com.sparta.gaeppa.ai.dto;

import com.sparta.gaeppa.ai.entity.AIPrompt;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemPromptRequestDto {
    private String prompt;

    public AIPrompt toEntity() {
        return AIPrompt.builder()
                .prompt(prompt)
                .build();
    }
}
