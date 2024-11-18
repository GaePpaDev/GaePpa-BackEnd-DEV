package com.sparta.gaeppa.store.repository;

import com.sparta.gaeppa.profile.entity.Profile;
import com.sparta.gaeppa.store.entity.Favorite;
import com.sparta.gaeppa.store.entity.Store;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {
    Optional<Favorite> findByProfileAndStore(Profile profile, Store store);

    List<Favorite> findAllByProfileProfileId(UUID profile_profileId);
}
