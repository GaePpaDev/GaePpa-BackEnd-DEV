package com.sparta.gaeppa.security.jwts.entity;

import com.sparta.gaeppa.members.entity.MemberRole;
import java.util.UUID;
import lombok.Data;

@Data
public class AuthenticatedUserDto {

    private UUID memberId;

    private String email;

    private String username;

    private String password;

    private MemberRole memberRole;

    private boolean isActive;

    public AuthenticatedUserDto(UUID memberId, String email, String username, String password, MemberRole memberRole, boolean isActive) {
        this.memberId = memberId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.memberRole = memberRole;
        this.isActive = isActive;
    }

    public static AuthenticatedUserDto createAuthenticatedUserDto(UUID memberId, MemberRole role, boolean isActive) {
        return new AuthenticatedUserDto(memberId, null, null, null, role, isActive);
    }
}
