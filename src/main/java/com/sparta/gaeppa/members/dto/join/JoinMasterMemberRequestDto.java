package com.sparta.gaeppa.members.dto.join;

import com.sparta.gaeppa.members.entity.LoginType;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinMasterMemberRequestDto {

    @Email
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$", message = "Password must contain at least one digit and one special character")
    private String password;

    private String emailToken;

    private boolean isCertifyByMail;

    private String providerUserId;

    // 커스텀 빌더 생성
    public static JoinMasterMemberRequestDtoBuilder builder() {
        return new JoinMasterMemberRequestDtoBuilder();
    }

    public static class JoinMasterMemberRequestDtoBuilder {
        private String email;
        private String username;
        private String password;
        private String emailToken = UUID.randomUUID().toString();
        private boolean isCertifyByMail = true;
        private String providerUserId = "GENERAL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        public JoinMasterMemberRequestDtoBuilder email(String email) {
            this.email = email;
            return this;
        }

        public JoinMasterMemberRequestDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public JoinMasterMemberRequestDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public JoinMasterMemberRequestDto build() {
            return new JoinMasterMemberRequestDto(email, username, password, emailToken, isCertifyByMail, providerUserId);
        }
    }

    // 엔티티로 변환 메서드 (PasswordEncoder 주입)
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password)) // 암호화 처리
                .emailToken(emailToken)
                .isCertifyByMail(isCertifyByMail)
                .providerUserId(providerUserId)
                .loginType(LoginType.GENERAL)
                .role(MemberRole.MASTER)
                .build();
    }
}