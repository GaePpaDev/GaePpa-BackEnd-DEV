package com.sparta.gaeppa.members.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = "provider_user_id")
})
@Entity
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID memberId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @Column(name = "provider_user_id", unique = true, nullable = false)
    private String providerUserId; // Provider + "-" + provider Id 형식

    @Column(name = "email_token")
    private String emailToken;

    @Column(name = "is_certified_email", nullable = false)
    private boolean isCertifyByMail = false;

    @Column(nullable = false)
    private boolean isActivated = true; // 기본 활성화 상태로 설정

    @Column(name = "account_non_locked", nullable = false)
    private boolean isAccountNonLocked = true; // 계정이 기본적으로 잠기지 않음

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 계정 활성화 메서드
    public void activateAccount() {
        this.isActivated = true;
    }

    // 이메일 인증 메서드
    public void certifyEmail() {
        this.isCertifyByMail = true;
    }

    public Member(String email, String password, String username, MemberRole role, LoginType loginType, String providerUserId, String emailToken, boolean isCertifyByMail, boolean isActivated) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.loginType = loginType;
        this.providerUserId = providerUserId;
        this.emailToken = emailToken;
        this.isCertifyByMail = isCertifyByMail;
        this.isActivated = isActivated;
    }

    @Builder
    private Member(String email, String username, String password, String emailToken, String providerUserId, LoginType loginType, MemberRole role, boolean isCertifyByMail) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.emailToken = emailToken;
        this.providerUserId = providerUserId;
        this.loginType = loginType;
        this.role = role;
        this.isCertifyByMail = isCertifyByMail;
    }

    public void setLastLoginDate(LocalDateTime now) {
        this.lastLoginAt = now;
    }

    public void updateRole(MemberRole newRole) {
        this.role = newRole;
    }

    // 일반 회원 생성 메서드

    // 소셜 회원 생성 메서드

    // 관리자 생성 메서드

}
