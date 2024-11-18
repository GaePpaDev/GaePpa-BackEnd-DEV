package com.sparta.gaeppa.ai.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gemini1_5ResponseDto {
    private List<Candidate> candidates;
    private PromptFeedback promptFeedback;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Candidate {
        private Content content;
        private String finishReason;
        private int index;
        private List<SafetyRating> safetyRatings;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Content {
        private List<Part> parts;
        private String role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Part {
        private String text;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SafetyRating {
        private String category;
        private String probability;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PromptFeedback {
        private List<SafetyRating> safetyRatings;
    }
}