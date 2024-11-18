package com.sparta.gaeppa.security.jwts.repository;

import com.sparta.gaeppa.security.jwts.entity.Refresh;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, UUID> {

    Boolean existsByRefreshToken(String refresh);

    void deleteByRefreshToken(String refresh);

    Optional<Refresh> findByRefreshToken(String refresh);

    Optional<Refresh> findByMemberMemberId(UUID memberId);
}
