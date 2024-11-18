package com.sparta.gaeppa.ai.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Gemini1_5TextRequestDto {
    private List<Content> contents;

    // text 값을 받아서 초기화하는 생성자
    @Builder
    public Gemini1_5TextRequestDto(String text) {
        Content content = new Content(text);
        this.contents = new ArrayList<>();
        this.contents.add(content);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Content {
        private List<Part> parts;

        public Content(String text) {
            Part part = new Part(text);
            this.parts = new ArrayList<>();
            this.parts.add(part);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Part {
        private String text;

        public Part(String text) {
            this.text = text;
        }
    }
}