package com.sparta.gaeppa.members.dto.admin;

import com.sparta.gaeppa.members.entity.LoginType;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberRole;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberResponseDto {

    private UUID memberId;
    private String email;
    private String username;
    private MemberRole role;
    private LoginType loginType;
    private String providerUserId;
    private boolean isCertifyByMail;
    private boolean isActivated;
    private boolean isAccountNonLocked;
    private LocalDateTime lastLoginAt;

    // Entity -> DTO 변환 메서드
    public static MemberResponseDto fromEntity(Member member) {
        return MemberResponseDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .username(member.getUsername())
                .role(member.getRole())
                .loginType(member.getLoginType())
                .providerUserId(member.getProviderUserId())
                .isCertifyByMail(member.isCertifyByMail())
                .isActivated(member.isActivated())
                .isAccountNonLocked(member.isAccountNonLocked())
                .lastLoginAt(member.getLastLoginAt())
                .build();
    }
}
