package com.sparta.gaeppa.security.jwts.entity;

import com.sparta.gaeppa.members.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "p_refresh")
public class Refresh {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "refresh_id")
    private UUID id;

    @Column(name = "refresh_token")
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TIMESTAMP") // MySQL의 경우
    private LocalDateTime expiration;

    public Refresh(String refreshToken, Member member, LocalDateTime expiration) {
        this.refreshToken = refreshToken;
        this.member = member;
        this.expiration = expiration;
    }

    /**
     * 리프레시 토큰과 만료 시간을 동시에 업데이트
     *
     * @param newRefreshToken 새로 발급된 리프레시 토큰
     * @param newExpiration   새로 설정된 만료 시간
     */
    public void updateRefreshTokenAndExpiration(String newRefreshToken, LocalDateTime newExpiration) {
        this.refreshToken = newRefreshToken;
        this.expiration = newExpiration;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
