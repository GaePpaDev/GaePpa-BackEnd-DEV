package com.sparta.gaeppa.ai.model;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AIModelFactory {

    private final List<IAIModel> models;

    public IAIModel getModel(String modelName) {
        return models.stream()
                .filter(model -> model.getName().equals(modelName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Model not found"));
    }
}
