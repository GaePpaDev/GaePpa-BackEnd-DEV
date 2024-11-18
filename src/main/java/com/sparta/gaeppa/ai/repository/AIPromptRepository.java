package com.sparta.gaeppa.ai.repository;

import com.sparta.gaeppa.ai.entity.AIPrompt;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIPromptRepository extends JpaRepository<AIPrompt, UUID> {
    Optional<AIPrompt> findFirstByOrderByCreatedAtDesc();
}
