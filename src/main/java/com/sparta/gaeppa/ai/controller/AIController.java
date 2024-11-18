package com.sparta.gaeppa.ai.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.ai.dto.AssistProductDescriptionRequestDto;
import com.sparta.gaeppa.ai.dto.AssistProductDescriptionResponseDto;
import com.sparta.gaeppa.ai.dto.SystemPromptRequestDto;
import com.sparta.gaeppa.ai.dto.SystemPromptResponseDto;
import com.sparta.gaeppa.ai.service.AIService;
import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/assist/product-description")
    public ResponseEntity<ApiResult<AssistProductDescriptionResponseDto>> getProductDescription(
            @RequestBody AssistProductDescriptionRequestDto requestDto) {

        AssistProductDescriptionResponseDto responseDto = aiService.getProductDescription(requestDto);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping("/system-prompt")
    public ResponseEntity<ApiResult<SystemPromptResponseDto>> createSystemPrompt(
            @RequestBody SystemPromptRequestDto requestDto) {
        
        SystemPromptResponseDto responseDto = aiService.createSystemPrompt(requestDto);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @GetMapping("/system-prompt")
    public ResponseEntity<ApiResult<SystemPromptResponseDto>> getSystemPrompt() {

        SystemPromptResponseDto responseDto = aiService.getSystemPrompt();

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }
}
