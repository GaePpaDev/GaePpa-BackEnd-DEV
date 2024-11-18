package com.sparta.gaeppa.security.jwts.filters;

import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.security.jwts.entity.AuthenticatedUserDto;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import com.sparta.gaeppa.security.jwts.utils.JwtUtil;
import com.sparta.gaeppa.security.jwts.utils.WebCookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
@Slf4j
@RequiredArgsConstructor
public class JWTFilterV1 extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final WebCookieUtil webCookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        String refreshAuthorization = webCookieUtil.getCookieValue(request, "refreshAuthorization");

        // 로그: 요청 URL과 인증 헤더 정보
        log.debug("JWTFilterV1 - Processing request: {}, Authorization header: {}", request.getRequestURL(), authorization);

        // 로그인, 회원가입 요청의 경우 토큰 검사를 하지 않음
        if (requestURI.equals("/api/v1/members/login") || requestURI.equals("/api/v1/members/join") || requestURI.equals("/api/v1/members/master/join")) {
            log.debug("JWTFilterV1 - Skipping authentication for public endpoints: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (refreshAuthorization == null || !refreshAuthorization.startsWith("Bearer+")) {
            log.info("JWTFilterV1 - No valid refresh token provided. Skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = refreshAuthorization.substring(7);

        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("JWTFilterV1 - Invalid refresh token for request: {}", request.getRequestURL());
            handleUnauthorizedRedirect(response);
            return;
        }

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String accessToken = authorization.split(" ")[1];

            if (jwtUtil.isExpired(accessToken)) {
                UUID memberId = jwtUtil.getMemberId(accessToken);
                log.warn("JWTFilterV1 - Access token expired for member ID: {}, redirecting to login.", memberId);
                handleUnauthorizedRedirect(response);
                return;
            }
        }

        try {
            Authentication authToken = getAuthentication(refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("JWTFilterV1 - Successfully authenticated member ID: {}", jwtUtil.getMemberId(refreshToken));
        } catch (BadCredentialsException e) {
            log.error("JWTFilterV1 - Authentication failed: {}", e.getMessage());
            handleUnauthorizedRedirect(response);
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new BadCredentialsException("Invalid token");
        }

        UUID memberId = jwtUtil.getMemberId(token);
        MemberRole role = jwtUtil.getRole(token);

        AuthenticatedUserDto authenticatedUserDto = AuthenticatedUserDto.createAuthenticatedUserDto(memberId, role, true);
        CustomUserDetails customOAuth2User = new CustomUserDetails(authenticatedUserDto);

        log.debug("JWTFilterV1 - Created CustomUserDetails for member ID: {}", memberId);

        return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    }

    private void handleUnauthorizedRedirect(HttpServletResponse response) throws IOException {
        log.info("JWTFilterV1 - Unauthorized access, redirecting to login.");
        response.sendRedirect("/login?unauthorizedRedirect=true");
    }
}
