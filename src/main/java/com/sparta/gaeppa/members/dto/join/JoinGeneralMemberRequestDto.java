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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class JoinGeneralMemberRequestDto {

    @Email
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$", message = "Password must contain at least one digit and one special character")
    private String password;

    private String emailToken;

    // Generate a UUID for provider_user_id
    String uuid = UUID.randomUUID().toString().replace("-", "");
    String provider_user_id = "GENERAL-" + uuid.substring(0, 12);

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .emailToken(emailToken)
                .providerUserId(provider_user_id)
                .loginType(LoginType.GENERAL)
                .role(MemberRole.CUSTOMER)
                .build();
    }
}
