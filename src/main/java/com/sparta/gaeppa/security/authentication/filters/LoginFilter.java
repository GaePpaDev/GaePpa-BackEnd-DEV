package com.sparta.gaeppa.security.authentication.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.service.MemberService;
import com.sparta.gaeppa.security.jwts.service.RefreshService;
import com.sparta.gaeppa.security.jwts.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

@Slf4j
public class LoginFilter extends CustomJsonEmailPasswordAuthenticationFilter {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RefreshService refreshService;
    private final ObjectMapper objectMapper;

    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리

    public LoginFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                       MemberService memberService, JwtUtil jwtUtil, RefreshService refreshService) {
        super(new AntPathRequestMatcher("/v1/members/login", "POST"), authenticationManager, objectMapper);
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
        this.refreshService = refreshService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("LoginFilter - SecurityContext already contains authentication. Skipping.");
                return SecurityContextHolder.getContext().getAuthentication();
            }

            if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
                throw new AuthenticationServiceException("Invalid content type");
            }

            String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

            String email = usernamePasswordMap.get("email");
            String password = usernamePasswordMap.get("password");

            if (email == null || password == null) {
                throw new AuthenticationServiceException("Missing email or password");
            }

            Member authenticatedMember = memberService.authenticateMember(email, password);
            return new UsernamePasswordAuthenticationToken(authenticatedMember.getEmail(), password);
        } catch (Exception e) {
            log.error("LoginFilter - Error during authentication: {}", e.getMessage());
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Authentication failed");
        }
        return null;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        String email = authentication.getName();
        Member member = memberService.memberLogin(email);

        String newAccess = jwtUtil.createAccessToken("access", member.getMemberId().toString(),
                member.getRole().toString());
        String newRefresh = jwtUtil.createRefreshToken("refresh", member.getMemberId().toString(),
                member.getRole().toString());

        refreshService.saveOrUpdateRefreshEntity(member, newRefresh);
        addResponseData(response, newAccess, newRefresh);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 로그에 자세한 실패 원인 기록
        log.info("로그인에 실패했습니다: {}", failed.getMessage());

        // 클라이언트에게는 일반적인 실패 메시지만 제공
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("timestamp", System.currentTimeMillis());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        responseData.put("error", "Unauthorized");
        responseData.put("message", "로그인에 실패했습니다.");

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

    /**
     * 쿠키에 refreshToken 을 넣어주는 방식
     */
    private void addResponseData(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> responseData = new HashMap<>();
        responseData.put("accessToken", accessToken);

        Cookie refreshCookie = new Cookie("refreshAuthorization", "Bearer+" + refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
        response.addCookie(refreshCookie);

        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }

}
