package com.sparta.gaeppa.ai.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
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

@Entity
@Table(name = "p_ai_interaction_logs")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class AIInteractionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ai_interaction_log_id")
    private UUID id;

    @Column(name = "user_prompt", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String userPrompt;

    @Column(name = "ai_response", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String aiResponse;

    @Column(name = "ai_model_name", nullable = false)
    private String aiModelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_prompt_id")
    private AIPrompt aiPrompt;

    @Builder
    private AIInteractionLog(String userPrompt, String aiResponse, String aiModelName, AIPrompt aiPrompt) {
        this.userPrompt = userPrompt;
        this.aiResponse = aiResponse;
        this.aiModelName = aiModelName;
        this.aiPrompt = aiPrompt;
    }
}
