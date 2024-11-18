package com.sparta.gaeppa.security.jwts.dto;

import com.sparta.gaeppa.security.jwts.entity.Refresh;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshDto {
    @NotBlank(message = "refresh token is required")
    private String refreshToken;
    @NotBlank(message = "expiration date is required")
    private LocalDateTime expirationDate;

    @Builder
    public RefreshDto(String refreshToken, LocalDateTime expirationDate) {
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }

    public Refresh toEntity() {
        return Refresh.builder()
                .refreshToken(refreshToken)
                .expiration(expirationDate)
                .build();
    }
}
