package com.sparta.gaeppa.profile.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.profile.entity.Profile;
import com.sparta.gaeppa.profile.repository.ProfileRepository;
import com.sparta.gaeppa.store.entity.Favorite;
import com.sparta.gaeppa.store.entity.Store;
import com.sparta.gaeppa.store.repository.FavoriteRepository;
import com.sparta.gaeppa.store.repository.StoreRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final FavoriteRepository favoriteRepository;
    private final StoreRepository storeRepository;

    /**
     * 프로필 소개글 수정
     * @param memberId 멤버 ID
     * @param introduce 새로운 소개글
     * @return 수정된 Profile 객체
     * @throws ServiceException 프로필이 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public Profile updateProfileIntroduce(UUID memberId, String introduce) {
        Profile profile = profileRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for memberId: " + memberId));
        profile.updateIntroduce(introduce);
        return profileRepository.save(profile);
    }

    /**
     * 프로필 이미지 수정
     * @param memberId 프로필 ID
     * @param imgName 이미지 이름
     * @param imgPath 이미지 경로
     * @return 수정된 Profile 객체
     * @throws ServiceException 프로필이 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public Profile updateProfileImage(UUID memberId, String imgName, String imgPath) {
        Profile profile = profileRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for memberId: " + memberId));
        profile.setProfileImage(imgName, imgPath);
        return profileRepository.save(profile);
    }

    /**
     * 멤버 ID로 프로필 조회
     * @param memberId 멤버 ID
     * @return 조회된 Profile 객체
     * @throws IllegalArgumentException 멤버 ID에 해당하는 프로필이 없을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public Profile getProfileByMemberId(UUID memberId) {
        return profileRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for memberId: " + memberId));
    }

    /**
     * 즐겨찾기 토글
     * 스토어를 즐겨찾기 추가/삭제를 처리
     * @param profileId 프로필 ID
     * @param memberId 스토어 ID
     * @return 상태 메시지 ("Store added to favorites." 또는 "Store removed from favorites.")
     * @throws ServiceException 프로필 또는 스토어가 존재하지 않을 경우 예외 발생
     */
    @Transactional
    public String toggleFavorite(UUID profileId, UUID memberId) {
        // 프로필 조회
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PROFILE_NOT_FOUND));
        // 스토어 조회
        Store store = storeRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.STORE_NOT_FOUND));

        // 프로필,스토어 ->  즐겨찾기 조회
        Favorite favorite = favoriteRepository.findByProfileAndStore(profile, store)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.FAVORITE_NOT_FOUND));

        if (favorite != null) {
            favoriteRepository.delete(favorite);
            return "Store removed from favorites.";
        } else {
            Favorite newFavorite = Favorite.builder()
                    .profile(profile)
                    .store(store)
                    .build();
            favoriteRepository.save(newFavorite);
            return "Store added to favorites.";
        }
    }

    /**
     * 프로필 ID로 즐겨찾기 목록 조회
     * @param memberId 멤버 ID
     * @return 해당 프로필의 즐겨찾기 목록
     */
    @Transactional(readOnly = true)
    public List<Favorite> getFavoritesByProfile(UUID memberId) {
        Profile profile = profileRepository.findByMemberMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found for memberId: " + memberId));
        return favoriteRepository.findAllByProfileProfileId(profile.getProfileId());
    }

}
