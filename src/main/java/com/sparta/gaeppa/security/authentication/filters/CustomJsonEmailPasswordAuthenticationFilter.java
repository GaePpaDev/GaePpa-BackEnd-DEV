package com.sparta.gaeppa.security.authentication.filters;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;

public class CustomJsonEmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "email";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String passwordParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    /**
     * HTTP POST 요청만 허용하도록 설정하는 옵션. 기본값은 true 이며, POST 가 아닌 요청은 인증 시도 없이 예외가 발생합니다.
     */
    @Setter
    private boolean postOnly = true;

    private final ObjectMapper objectMapper;

    public CustomJsonEmailPasswordAuthenticationFilter(AntPathRequestMatcher requestMatcher, AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        super(requestMatcher, authenticationManager);
        this.objectMapper = objectMapper;
    }

    /**
     * 인증 처리 메소드.
     * JSON 데이터를 파싱하여 이메일과 패스워드를 추출하고,
     * UsernamePasswordAuthenticationToken을 사용해 인증 요청을 수행합니다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        // 제네릭 타입을 new TypeReference 선언해줌으로써 컴파일시 제네릭타입으로 검증.
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, new TypeReference<Map<String, String>>() {
        });

        String email = usernamePasswordMap.get(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = usernamePasswordMap.get(SPRING_SECURITY_FORM_PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);
        //principal 과 credentials 전달

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    /**
     * 요청에서 패스워드를 추출하는 메소드.
     * @param request 요청 객체
     * @return 패스워드 문자열
     */
    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    /**
     * 요청에서 유저명을 추출하는 메소드.
     * @param request 요청 객체
     * @return 유저명 문자열
     */
    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    /**
     * 인증 요청의 상세 정보를 설정하는 메소드.
     * @param request 요청 객체
     * @param authRequest 인증 요청 객체
     */
    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    /**
     * 유저명을 가져올 파라미터 이름을 설정하는 메소드.
     * @param usernameParameter 유저명 파라미터 이름
     */
    public void setUsernameParameter(String usernameParameter) {
        Assert.hasText(usernameParameter, "Username parameter must not be empty or null");
        this.usernameParameter = usernameParameter;
    }

    /**
     * 패스워드를 가져올 파라미터 이름을 설정하는 메소드.
     * @param passwordParameter 패스워드 파라미터 이름
     */
    public void setPasswordParameter(String passwordParameter) {
        Assert.hasText(passwordParameter, "Password parameter must not be empty or null");
        this.passwordParameter = passwordParameter;
    }

    public final String getUsernameParameter() {
        return this.usernameParameter;
    }

    public final String getPasswordParameter() {
        return this.passwordParameter;
    }
}
