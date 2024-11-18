package com.sparta.gaeppa.ai.service;

import com.sparta.gaeppa.ai.dto.AssistProductDescriptionRequestDto;
import com.sparta.gaeppa.ai.dto.AssistProductDescriptionResponseDto;
import com.sparta.gaeppa.ai.dto.SystemPromptRequestDto;
import com.sparta.gaeppa.ai.dto.SystemPromptResponseDto;
import com.sparta.gaeppa.ai.entity.AIInteractionLog;
import com.sparta.gaeppa.ai.entity.AIPrompt;
import com.sparta.gaeppa.ai.model.AIModelFactory;
import com.sparta.gaeppa.ai.repository.AIInteractionLogRepository;
import com.sparta.gaeppa.ai.repository.AIPromptRepository;
import com.sparta.gaeppa.global.exception.AIException;
import com.sparta.gaeppa.global.exception.ExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {

    private final AIModelFactory aiModelFactory;
    private final AIInteractionLogRepository aiInteractionLogRepository;
    private final AIPromptRepository aiPromptRepository;

    public AssistProductDescriptionResponseDto getProductDescription(AssistProductDescriptionRequestDto requestDto) {

//        String prompt = """
//                    음식 이름과 간단한 설명을 기반으로, 고객이 메뉴를 선택하도록 유도하는 매력적인 음식 설명을 작성해주세요.
//                    설명은 50자 이내로 작성하되, 자극적이고 입맛을 돋우는 문구를 사용하여 손님들의 관심을 끌도록 합니다.
//
//                    **주요 포인트:**
//                    - 음식을 한 번 먹으면 계속 먹고 싶어지는 유혹적인 문구
//                    - 손님들이 이 음식을 선택하지 않을 수 없도록 만드는 설득력 있는 설명
//                    - 50자 이내로 간결하면서도 강렬하게 어필
//
//                    **예시 1:**
//                    입력:
//                    이름: 불고기
//                    설명: 손님들이 불고기를 선택하도록 유도하는 설명을 작성해주세요.
//
//                    출력:
//                    우리집 불고기는 풍미 가득한 양념과 부드러운 고기, 그 맛이 한번 먹으면 멈출 수 없어요.
//
//                    **예시 2:**
//                    입력:
//                    이름: 치킨
//                    설명: 맛있는 우리집 치킨
//
//                    출력:
//                    바삭하고 육즙 가득한 우리 치킨, 한 입 먹으면 계속 손이 가는 맛!
//
//                    이와 같은 형식으로 유혹적인 음식 설명을 만들어주세요.

//                """;

        String AIModelName = "Gemini1.5";

        AIPrompt prompt = aiPromptRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(() -> new AIException(
                ExceptionStatus.AI_PROMPT_NOT_FOUND));

        String SystemPrompt = prompt.getPrompt();

        String userPrompt = "이름: " + requestDto.getProductName() + "\n" + "설명: " + requestDto.getProductDescription();

        String aiResponse = aiModelFactory.getModel(AIModelName).processTextRequest(SystemPrompt + userPrompt);

        aiInteractionLogRepository.save(AIInteractionLog.builder()
                .aiModelName(AIModelName)
                .aiPrompt(prompt)
                .userPrompt(userPrompt)
                .aiResponse(aiResponse)
                .build());

        return AssistProductDescriptionResponseDto.builder()
                .productDescriptionRecommendation(aiResponse)
                .build();
    }

    public SystemPromptResponseDto createSystemPrompt(SystemPromptRequestDto requestDto) {
        AIPrompt aiPrompt = aiPromptRepository.save(requestDto.toEntity());

        return SystemPromptResponseDto.from(aiPrompt);
    }

    public SystemPromptResponseDto getSystemPrompt() {
        AIPrompt aiPrompt = aiPromptRepository.findFirstByOrderByCreatedAtDesc().orElseThrow(() -> new AIException(
                ExceptionStatus.AI_PROMPT_NOT_FOUND));

        return SystemPromptResponseDto.from(aiPrompt);
    }
}
