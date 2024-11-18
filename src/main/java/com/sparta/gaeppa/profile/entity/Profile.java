package com.sparta.gaeppa.profile.entity;

import com.sparta.gaeppa.global.base.BaseEntity;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.entity.MemberGender;
import com.sparta.gaeppa.store.entity.Favorite;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_profile")
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "profile_id")
    private UUID profileId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "profile_img_name")
    private String profileImgName;
    @Column(name = "profile_img_path")
    private String profileImgPath;

    @Lob
    private String introduce;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_gender")
    private MemberGender memberGender;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites = new ArrayList<>(); // 즐겨찾기 목록

    public Profile(Member member, String profileImgName, String profileImgPath, String introduce,
                   MemberGender memberGender) {
        this.member = member;
        this.profileImgName = profileImgName;
        this.profileImgPath = profileImgPath;
        this.introduce = introduce;
        this.memberGender = memberGender;
    }

    /**
     * 최초 회원가입 시, 기본적으로 만들어주는 프로필. Profile 과 Member 는 강한 종속성을 띱니다.
     * Member 는 인증, 인가에 대한 책임
     * Profile 은 회원 서비스에 대한 책임을 갖습니다.
      */
    public static Profile createMemberProfile(Member member) {
        return new Profile(member, "프로필이미지없음", null, "소개 없음", MemberGender.NON_PROVIDED);
    }

    // 프로필 이미지 설정 메서드
    public void setProfileImage(String profileImgName, String profileImgPath) {
        this.profileImgName = profileImgName;
        this.profileImgPath = profileImgPath;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }
}
