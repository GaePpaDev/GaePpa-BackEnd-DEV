package com.sparta.gaeppa.members.dto.join;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberRole;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinResponseDto {
    private UUID memberId;
    private String email;
    private String username;
    private MemberRole role;

    public static JoinResponseDto from(Member member) {
        return JoinResponseDto.builder()
                .memberId(member.getMemberId())
                .username(member.getUsername())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
