package com.sparta.gaeppa.security.jwts.service;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.security.jwts.dto.RefreshDto;
import com.sparta.gaeppa.security.jwts.entity.Refresh;
import com.sparta.gaeppa.security.jwts.repository.RefreshRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class RefreshService {

    private final RefreshRepository refreshRepository;
    private static final Long REFRESH_TOKEN_EXPIRATION_PERIOD = 3600L * 24 * 7; // 7일


    /**
     * [Refresh 토큰 - DB 에서 관리] 로그인 성공 시 리프레시 토큰을 업데이트하거나 새로 저장합니다.
     *
     * @param member          회원의 PK로 리프레시 토큰 조회
     * @param newRefreshToken 새롭게 생성된 리프레시 토큰
     */
    @Transactional
    public void saveOrUpdateRefreshEntity(Member member, String newRefreshToken) {
        LocalDateTime expirationDateTime = calculateExpirationDateTime();

        Refresh refreshEntity = refreshRepository.findByMemberMemberId(member.getMemberId())
                .orElse(new Refresh(newRefreshToken, member, expirationDateTime));

        // 기존 리프레시 토큰이 있으면 갱신, 없으면 새로 설정
        refreshEntity.updateRefreshTokenAndExpiration(newRefreshToken, expirationDateTime);
        refreshRepository.save(refreshEntity);
    }

    private static LocalDateTime calculateExpirationDateTime() {
        return LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_PERIOD);
    }

    private void updateRefreshToken(Refresh existingRefresh, String newRefreshToken, LocalDateTime expirationDateTime) {
        RefreshDto refreshDto = RefreshDto.builder().refreshToken(newRefreshToken).expirationDate(expirationDateTime)
                .build();
        existingRefresh.updateRefreshTokenAndExpiration(refreshDto.getRefreshToken(), refreshDto.getExpirationDate());
        refreshRepository.save(existingRefresh);
    }

    private void createNewRefreshEntity(Member member, String newRefreshToken, LocalDateTime expirationDateTime) {
        Refresh newRefreshEntity = new Refresh(newRefreshToken, member, expirationDateTime);
        refreshRepository.save(newRefreshEntity);
    }
}
