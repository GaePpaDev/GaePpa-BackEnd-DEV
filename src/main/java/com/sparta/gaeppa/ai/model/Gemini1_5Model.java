package com.sparta.gaeppa.ai.model;

import com.sparta.gaeppa.ai.dto.Gemini1_5ResponseDto;
import com.sparta.gaeppa.ai.dto.Gemini1_5TextRequestDto;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class Gemini1_5Model implements IAIModel {

   public static final String MODEL_NAME = "Gemini1.5";

   private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

   private final RestTemplate restTemplate;

   @Value("${google.ai.key}")
   private String apiKey;

   @Override
   public String getName() {
       return MODEL_NAME;
   }

   @Override
   public String processTextRequest(String requestText) {

       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);

       RequestEntity<Gemini1_5TextRequestDto> requestEntity = new RequestEntity<Gemini1_5TextRequestDto>(
               Gemini1_5TextRequestDto.builder().text(requestText).build(), headers, HttpMethod.POST,
               URI.create(URL + apiKey));

       return Objects.requireNonNull(restTemplate.exchange(requestEntity, Gemini1_5ResponseDto.class).getBody())
               .getCandidates().getFirst()
               .getContent().getParts().getFirst().getText();

   }
}
