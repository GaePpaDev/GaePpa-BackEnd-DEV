package com.sparta.gaeppa.members.repository;

import com.sparta.gaeppa.members.entity.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    Boolean existsByEmail(String email);

    // 식별자를 Email 에서 Social Id 로 변경함으로써, email 은 2개이상의 계정으로 중복될 수 있음.
    Optional<Member> findByEmail(String email);

    List<Member> findAllByEmail(String email);

    Optional<Member> findByProviderUserId(String socialId);

    Optional<Member> findByEmailToken(String token);

    Long countByEmail(String email);
}
