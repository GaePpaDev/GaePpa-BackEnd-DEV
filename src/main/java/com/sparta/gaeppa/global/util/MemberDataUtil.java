package com.sparta.gaeppa.global.util;

import com.sparta.gaeppa.members.entity.LoginType;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberRole;
import com.sparta.gaeppa.members.repository.MemberRepository;
import com.sparta.gaeppa.profile.entity.Profile;
import com.sparta.gaeppa.profile.repository.ProfileRepository;
import com.sparta.gaeppa.store.dto.StoreCreateRequestDto;
import com.sparta.gaeppa.store.entity.StoreCategory;
import com.sparta.gaeppa.store.service.StoreCategoryService;
import com.sparta.gaeppa.store.service.StoreService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberDataUtil {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;
    private final StoreCategoryService storeCategoryService;

    public void generateMemberData() {
        // 고객 회원 생성
        createCustomerMember("customer1@pushop.com", "roQkfmsalswhr!", "customer1");
        createCustomerMember("customer2@pushop.com", "roQkfmsalswhr!", "customer2");
        createCustomerMember("customer3@pushop.com", "roQkfmsalswhr!", "customer3");

        // 스토어주인 회원 생성
        Member owner1 = createOwnerMember("owner1@pushop.com", "roQkfmsalswhr!", "owner1");
        Member owner2 = createOwnerMember("owner2@pushop.com", "roQkfmsalswhr!", "owner2");
        Member owner3 = createOwnerMember("owner3@pushop.com", "roQkfmsalswhr!", "owner3");

        // OWNER 회원들에게 스토어 생성
        createStoreForOwner(owner1, "한식스토어1", "한식");
        createStoreForOwner(owner2, "중식스토어2", "중식");
        createStoreForOwner(owner3, "양식스토어3", "양식");

        // 매니저 회원 생성
        createManagerMember("manager1@pushop.com", "roQkfmsalswhr!", "manager1");
        createManagerMember("manager2@pushop.com", "roQkfmsalswhr!", "manager2");
        createManagerMember("manager3@pushop.com", "roQkfmsalswhr!", "manager3");

        // 마스터 회원 생성
        createMasterMember("master1@pushop.com", "roQkfmsalswhr!", "master1");
        createMasterMember("master2@pushop.com", "roQkfmsalswhr!", "master2");
        createMasterMember("master3@pushop.com", "roQkfmsalswhr!", "master3");
    }

    private void createCustomerMember(String email, String password, String username) {
        Member customerMember = buildMember(email, password, username, MemberRole.CUSTOMER);
        saveMemberAndProfile(customerMember);
    }

    private Member createOwnerMember(String email, String password, String username) {
        Member ownerMember = buildMember(email, password, username, MemberRole.OWNER);
        return saveMemberAndProfile(ownerMember);
    }

    private Member buildMember(String email, String password, String username, MemberRole role) {
        String providerUserId = generateProviderUserId();
        String emailToken = UUID.randomUUID().toString();

        return Member.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .emailToken(emailToken)
                .providerUserId(providerUserId)
                .loginType(LoginType.GENERAL)
                .role(role)
                .isCertifyByMail(true)
                .build();
    }

    private void createManagerMember(String email, String password, String username) {
        Member managerMember = buildMember(email, password, username, MemberRole.MANAGER);
        saveMemberAndProfile(managerMember);
    }

    private void createMasterMember(String email, String password, String username) {
        Member masterMember = buildMember(email, password, username, MemberRole.MASTER);
        saveMemberAndProfile(masterMember);
    }


    private Member saveMemberAndProfile(Member member) {
        Member savedMember = memberRepository.save(member);
        Profile profile = Profile.createMemberProfile(savedMember);
        profileRepository.save(profile);
        return savedMember;
    }

    private String generateProviderUserId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "GENERAL-" + uuid.substring(0, 12);
    }

    private void createStoreForOwner(Member owner, String storeName, String categoryName) {
        // 카테고리 생성
        StoreCategory storeCategory = storeCategoryService.createIfNotExists(categoryName);

        // 스토어 생성 요청 DTO
        StoreCreateRequestDto storeRequestDto = StoreCreateRequestDto.builder()
                .storeName(storeName)
                .storeCategoryName(storeCategory.getCategoryName())
                .storeAddress("서울특별시 강남구 테헤란로 123")
                .storeTelephone("010-1234-5678")
                .storeIntroduce("이곳은 최고의 장소입니다.")
                .businessTime("09:00 - 22:00")
                .build();

        // 스토어 생성 (Owner ID 사용)
        storeService.createOwnerStore(storeRequestDto, owner.getMemberId());
    }
}
