package com.sparta.gaeppa.global.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host); // SMTP 서버 주소
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username); // Naver 아이디
        javaMailSender.setPassword(password); // Naver 비밀번호 또는 앱 비밀번호
        javaMailSender.setDefaultEncoding("UTF-8");
        javaMailSender.setJavaMailProperties(getMailProperties()); // 메일 인증 서버 정보 설정

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); // SMTP 인증 활성화
        properties.put("mail.smtp.ssl.enable", "true"); // SSL 사용
        properties.put("mail.smtp.ssl.trust", "smtp.naver.com"); // SSL 신뢰할 서버 설정
        properties.put("mail.smtp.connectiontimeout", "5000"); // 연결 타임아웃 설정
        properties.put("mail.smtp.timeout", "5000"); // 타임아웃 설정
        properties.put("mail.smtp.writetimeout", "5000"); // 쓰기 타임아웃 설정
        properties.put("mail.debug", "true"); // 디버그 모드 활성화

        return properties;
    }
}
