package com.sparta.gaeppa.security.jwts.entity;

import com.sparta.gaeppa.members.entity.MemberRole;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthenticatedUserDto authenticatedUserDto;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of((GrantedAuthority) () -> authenticatedUserDto.getMemberRole().toString());
    }

    @Override
    public String getPassword() {
        return authenticatedUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return authenticatedUserDto.getEmail();
    }

    public UUID getMemberId() { return authenticatedUserDto.getMemberId(); }

    public MemberRole getMemberRole(){
        return authenticatedUserDto.getMemberRole();
    }

    @Override
    public boolean isAccountNonExpired() {
        return authenticatedUserDto.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return authenticatedUserDto.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return authenticatedUserDto.isActive();
    }

    @Override
    public boolean isEnabled() {
        return authenticatedUserDto.isActive();
    }
}
