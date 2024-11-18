package com.sparta.gaeppa.store.repository;

import com.sparta.gaeppa.store.entity.Store;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByMemberMemberId(UUID memberId);

    Store findByStoreName(String storeName);

    Optional<Store> getStoreByMember_MemberId(UUID memberId);
}
