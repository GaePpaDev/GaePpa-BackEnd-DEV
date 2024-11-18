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
public class JWTFilterV2 extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 이미 Security Context Holder 에 인증 정보가 있는 경우 인증 로직 건너뛰기
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.debug("JWTFilterV2 - Authentication already exists in SecurityContext. Skipping.");
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();
        String authorization = request.getHeader("Authorization");
        String refreshAuthorization = WebCookieUtil.getCookieValue(request, "refreshAuthorization");

        log.debug("JWTFilterV2 - Processing request: {}, Authorization header: {}", request.getRequestURL(), authorization);

        if (requestURI.equals("/api/v1/members/login") || requestURI.equals("/api/v1/members/join") || requestURI.equals("/api/v1/members/master/join")) {
            log.debug("JWTFilterV2 - Skipping authentication for public endpoints: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        if (refreshAuthorization == null || !refreshAuthorization.startsWith("Bearer+")) {
            log.info("JWTFilterV2 - No valid refresh token provided. Skipping authentication.");
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = refreshAuthorization.substring(7);

        if (!jwtUtil.validateToken(refreshToken)) {
            log.warn("JWTFilterV2 - Invalid refresh token for request: {}", request.getRequestURL());
            handleUnauthorizedRedirect(response);
            return;
        }

        try {
            Authentication authToken = getAuthentication(refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("JWTFilterV2 - Successfully authenticated member ID: {}", jwtUtil.getMemberId(refreshToken));
        } catch (BadCredentialsException e) {
            log.error("JWTFilterV2 - Authentication failed: {}", e.getMessage());
            handleUnauthorizedRedirect(response);
            return;
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

        log.debug("JWTFilterV2 - Created CustomUserDetails for member ID: {}", memberId);

        return new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
    }

    private void handleUnauthorizedRedirect(HttpServletResponse response) throws IOException {
        log.info("JWTFilterV1 - Unauthorized access, redirecting to login.");
        response.sendRedirect("/login?unauthorizedRedirect=true");
    }
}
