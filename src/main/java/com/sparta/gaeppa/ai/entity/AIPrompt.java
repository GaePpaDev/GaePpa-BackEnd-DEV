package com.sparta.gaeppa.ai.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_ai_prompts")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AIPrompt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ai_prompt_id")
    private UUID id;

    @Column(name = "prompt", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String prompt;

    @Builder
    private AIPrompt(String prompt) {
        this.prompt = prompt;
    }
}
