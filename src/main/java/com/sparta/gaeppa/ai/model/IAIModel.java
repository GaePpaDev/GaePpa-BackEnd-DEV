package com.sparta.gaeppa.ai.model;

public interface IAIModel {
    String getName();

    String processTextRequest(String requestText);
}
