package com.sparta.gaeppa.profile.repository;

import com.sparta.gaeppa.profile.entity.Profile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile> findByMemberMemberId(UUID memberId);
}
