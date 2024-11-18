package com.sparta.gaeppa.security.jwts.service;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.members.repository.MemberRepository;
import com.sparta.gaeppa.security.jwts.dto.RefreshDto;
import com.sparta.gaeppa.security.jwts.entity.Refresh;
import com.sparta.gaeppa.security.jwts.repository.RefreshRepository;
import com.sparta.gaeppa.security.jwts.utils.JwtUtil;
import com.sparta.gaeppa.security.jwts.utils.WebCookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;
    private final MemberRepository memberRepository;

    private static final Long ACCESS_TOKEN_EXPIRATION = 60L * 30; // 30 분
    private static final Long REFRESH_TOKEN_EXPIRATION = 3600L * 24 * 7; // 7일

    public String reissueAccessToken(HttpServletRequest request) {
        String refreshToken = getRefreshTokenFromRequest(request);

        // 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 리프레시 토큰 검증 및 만료 체크
        Refresh refresh = refreshRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NoSuchElementException("Refresh token not found"));

        if (refresh.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ExpiredJwtException(null, null, "Refresh token expired");
        }

        UUID memberId = jwtUtil.getMemberId(refreshToken);
        MemberRole role = jwtUtil.getRole(refreshToken);

        // 새 액세스 토큰 생성
        return jwtUtil.createAccessToken("access", String.valueOf(memberId), role.toString());
    }

    public void validateAndReissueTokens(HttpServletResponse response, String beforeRefresh) throws ClassNotFoundException {
        if (!jwtUtil.validateToken(beforeRefresh)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        UUID memberId = jwtUtil.getMemberId(beforeRefresh);
        MemberRole role = jwtUtil.getRole(beforeRefresh);

        // 기존 refresh 토큰이 DB에 존재하는지 확인
        if (!refreshRepository.existsByRefreshToken(beforeRefresh)) {
            throw new IllegalStateException("Refresh token not found in DB");
        }

        // 새로운 토큰 생성
        String newAccessToken = jwtUtil.createAccessToken("access", String.valueOf(memberId), role.toString());
        String newRefreshToken = jwtUtil.createRefreshToken("refresh", String.valueOf(memberId), role.toString());

        // 기존 refresh 토큰 삭제
        refreshRepository.deleteByRefreshToken(beforeRefresh);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found for given token"));

        // 새로운 refresh 토큰 저장
        saveRefreshEntity(member, newRefreshToken);

        // 응답 설정
        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(WebCookieUtil.createCookieLocal("refreshAuthorization", "Bearer+" + newRefreshToken));
    }

    private void saveRefreshEntity(Member member, String newRefreshToken) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(REFRESH_TOKEN_EXPIRATION);
        RefreshDto refreshDto = RefreshDto.builder()
                .refreshToken(newRefreshToken)
                .expirationDate(expiration).build();

        Refresh refresh = refreshRepository.findByMemberMemberId(member.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Refresh entity not found for member"));

        refresh.updateRefreshToken(refreshDto.getRefreshToken());
        refreshRepository.save(refresh);
    }

    public void handleExpiredRefresh(HttpServletResponse response, String memberId) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write("{\"message\":\"Refresh token expired\"}");
        log.info("Refresh token expired for memberId: {}", memberId);
    }

    public Map<String, String> reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshTokenFromRequest(request);

        // 리프레시 토큰 검증
        jwtUtil.validateToken(refreshToken);

        // 리프레시 토큰 재발급 및 저장
        UUID memberId = jwtUtil.getMemberId(refreshToken);
        MemberRole role = jwtUtil.getRole(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken("access", String.valueOf(memberId), role.toString());
        String newRefreshToken = jwtUtil.createRefreshToken("refresh", String.valueOf(memberId), role.toString());

        refreshRepository.deleteByRefreshToken(refreshToken);

        Refresh refreshEntity = refreshRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("Refresh token not found"));
        refreshEntity.updateRefreshToken(newRefreshToken);
        refreshRepository.save(refreshEntity);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return tokens;
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new IllegalArgumentException("No cookies found in the request");
        }
        return Arrays.stream(cookies)
                .filter(cookie -> "refreshAuthorization".equals(cookie.getName()))
                .map(cookie -> cookie.getValue().substring(7)) // Remove "Bearer+"
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Refresh token not found in cookies"));
    }

}
