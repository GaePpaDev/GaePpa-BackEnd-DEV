package com.sparta.gaeppa.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.gaeppa.members.service.MemberService;
import com.sparta.gaeppa.security.authentication.filters.LoginFilter;
import com.sparta.gaeppa.security.jwts.filters.JWTFilterV2;
import com.sparta.gaeppa.security.jwts.service.RefreshService;
import com.sparta.gaeppa.security.jwts.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationEntryPointConfig authenticationEntryPoint;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RefreshService refreshService;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public JWTFilterV2 jwtFilter() {
        return new JWTFilterV2(jwtUtil);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginFilter loginFilter() throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationConfiguration.getAuthenticationManager(), objectMapper,
                memberService, jwtUtil, refreshService);
        loginFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/v1/members/login", "POST"));
        return loginFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 인가 설정
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/v1/members/login", "/api/v1/members/join", "/api/v1/members/master/join",
                                        "/error").permitAll()
                                .requestMatchers("/api/v1/admin/**").hasAnyRole("MANAGER", "MASTER")
                                .requestMatchers("/api/v1/profiles/**").authenticated()
                                .requestMatchers("/api/v1/stores/**").hasAnyRole("OWNER", "MANAGER", "MASTER")
                                .requestMatchers("/api/v1/orders/**", "/api/v1/payments/**", "/api/v1/reviews/**")
                                .authenticated()
                                .requestMatchers("/api/v1/product-categories/**").authenticated()
//                        .anyRequest().denyAll()
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-resources/**",
                                        "/webjars/**"
                                ).permitAll()
                                .anyRequest().permitAll()
                )// 나머지 요청은 모두 차단
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint));

        // 필터 순서: LoginFilter -> JWTFilter
        http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtFilter(), LoginFilter.class);

        return http.build();
    }

}
