package com.sparta.gaeppa.global.base;

import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<UUID> {

    @Override
    @NonNull
    public Optional<UUID> getCurrentAuditor() {
        return getCurrentUserId();
    }

    public Optional<UUID> getCurrentUserId() {
        return getCustomUserDetails()
                .map(CustomUserDetails::getMemberId);
    }

    public Optional<String> getCurrentUserRole() {
        return getCustomUserDetails()
                .map(user -> user.getMemberRole().toString());
    }

    // [2024.11.17] 접근 제어자를 private 으로 둠으로써, UserDetails 자체를 꺼내는 것을 지양했다.
    private Optional<CustomUserDetails> getCustomUserDetails() {
        if (SecurityContextHolder.getContext() == null) {
            return Optional.empty();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return Optional.of((CustomUserDetails) authentication.getPrincipal());
        }

        return Optional.empty();
    }
}