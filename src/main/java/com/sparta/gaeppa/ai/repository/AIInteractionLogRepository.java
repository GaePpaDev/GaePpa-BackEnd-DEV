package com.sparta.gaeppa.ai.repository;

import com.sparta.gaeppa.ai.entity.AIInteractionLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AIInteractionLogRepository extends JpaRepository<AIInteractionLog, UUID> {
}
