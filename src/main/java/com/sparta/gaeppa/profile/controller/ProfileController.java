package com.sparta.gaeppa.profile.controller;

import com.sparta.gaeppa.global.util.ApiResponseUtil;
import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.profile.entity.Profile;
import com.sparta.gaeppa.profile.service.ProfileService;
import com.sparta.gaeppa.store.entity.Favorite;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 멤버 ID로 프로필 조회
     * @param memberId 멤버 ID
     * @return 프로필 정보
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResult<Profile>> getProfileByMemberId(@PathVariable UUID memberId) {
        Profile profile = profileService.getProfileByMemberId(memberId);
        return ResponseEntity.ok(ApiResponseUtil.success(profile));
    }

    /**
     * 프로필 소개 수정
     * @param memberId 멤버 ID
     * @param introduce 새로운 소개글
     * @return 수정된 프로필 정보
     */
    @PutMapping("/introduce/{memberId}")
    public ResponseEntity<ApiResult<Profile>> updateIntroduce(
            @PathVariable UUID memberId,
            @RequestBody String introduce) {
        Profile updatedProfile = profileService.updateProfileIntroduce(memberId, introduce);
        return ResponseEntity.ok(ApiResponseUtil.success(updatedProfile));
    }

    /**
     * 프로필의 모든 즐겨찾기 조회
     * @param memberId 멤버 ID
     * @return 즐겨찾기 목록
     */
    @GetMapping("/favorites/{memberId}")
    public ResponseEntity<ApiResult<List<Favorite>>> getFavorites(@PathVariable UUID memberId) {
        List<Favorite> favorites = profileService.getFavoritesByProfile(memberId);
        return ResponseEntity.ok(ApiResponseUtil.success(favorites));
    }

    /**
     * 즐겨찾기 토글 API
     * @param profileId 프로필 ID
     * @param memberId 멤버 ID
     * @return 즐겨찾기 상태와 메시지
     */
    @PostMapping("/favorites/{memberId}")
    public ResponseEntity<ApiResult<String>> toggleFavorite(
            @RequestParam UUID profileId,
            @PathVariable UUID memberId) {
        // 서비스 계층에 즐겨찾기 토글 로직 위임
        String message = profileService.toggleFavorite(profileId, memberId);

        // 성공 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseUtil.success(message));
    }

}
