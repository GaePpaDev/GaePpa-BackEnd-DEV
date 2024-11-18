package com.sparta.gaeppa.members.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.members.dto.admin.MemberResponseDto;
import com.sparta.gaeppa.members.dto.join.JoinGeneralMemberRequestDto;
import com.sparta.gaeppa.members.dto.join.JoinMasterMemberRequestDto;
import com.sparta.gaeppa.members.dto.join.JoinResponseDto;
import com.sparta.gaeppa.members.entity.LoginType;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.repository.MemberRepository;
import com.sparta.gaeppa.profile.entity.Profile;
import com.sparta.gaeppa.profile.repository.ProfileRepository;
import com.sparta.gaeppa.security.jwts.entity.Refresh;
import com.sparta.gaeppa.security.jwts.repository.RefreshRepository;
import com.sparta.gaeppa.security.jwts.utils.WebCookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepositoryV1;
    private final PasswordEncoder passwordEncoder;
    private final RefreshRepository refreshRepository;
    private final ProfileRepository profileRepository;
    private final MemberEmailService memberEmailService;
    private final MemberRepository memberRepository;

    /**
     * 이메일 인증을 통한 회원가입
     * - 이메일과 닉네임 중복 여부를 확인 후 회원가입 처리
     * - 이메일 인증 메일 발송
     *
     * @param joinGeneralMemberRequestDto 회원가입 요청 DTO
     * @return 가입된 회원의 응답 DTO
     */
    @Transactional
    public JoinResponseDto joinMemberByEmailAuthentication(JoinGeneralMemberRequestDto joinGeneralMemberRequestDto) throws Exception {
        // 닉네임 유효성 검사
        if (joinGeneralMemberRequestDto.getUsername() == null) {
            throw new IllegalArgumentException("유저네임을 꼭 입력해야합니다.");
        }

        // 동일한 이메일이 존재하는지 유효성 검사
        validateExistedMemberByEmail(joinGeneralMemberRequestDto.getEmail());

        // 표준화된 128-bit의 고유 식별자 생성
        String emailToken = UUID.randomUUID().toString();

        // 회원 생성 및 저장
        Member joinMember = joinGeneralMemberRequestDto.toEntity(passwordEncoder);

        Member newJoinMember = memberRepositoryV1.save(joinMember);

        // 프로필 생성 및 저장
        Profile profile = Profile.createMemberProfile(newJoinMember);
        profileRepository.save(profile);

        // 이메일 인증 메일 전송
        sendVerificationEmail(newJoinMember.getEmail(), emailToken);

        return JoinResponseDto.from(newJoinMember);
    }

    /**
     * 이메일 인증 없이 관리자 회원가입
     * - 이메일 인증 과정을 생략
     *
     * @param joinMasterMemberRequestDto 관리자 회원가입 요청 DTO
     * @return 가입된 회원의 응답 DTO
     */
    @Transactional
    public JoinResponseDto joinMemberNoEmailAuthentication(JoinMasterMemberRequestDto joinMasterMemberRequestDto) throws Exception {
        // 닉네임 유효성 검사
        if (joinMasterMemberRequestDto.getUsername() == null) {
            throw new IllegalArgumentException("유저네임을 꼭 입력해야합니다.");
        }

        // 동일한 이메일이 존재하는지 유효성 검사
        validateExistedMemberByEmail(joinMasterMemberRequestDto.getEmail());

        // email Token 을 toEntity 내에서 알아서 설정해주고 저장. 이메일 인증 기능 생략
        // 회원 생성 및 저장
        Member joinMember = joinMasterMemberRequestDto.toEntity(passwordEncoder);
        joinMember.certifyEmail();
        log.debug("MemberService : member 의 이메일 인증 상태를 확인 합니다 : {}", joinMember.isCertifyByMail());

        memberRepositoryV1.save(joinMember);

        // 프로필 생성 및 저장
        Profile profile = Profile.createMemberProfile(joinMember);
        profileRepository.save(profile);

        return JoinResponseDto.from(joinMember);
    }

    /**
     * 일반 회원 로그인
     * - 이메일로 회원 조회 후 로그인 처리
     *
     * @param email 로그인 이메일
     * @return 로그인된 회원 객체
     */
    @Transactional
    public Member memberLogin(String email) {
        List<Member> generalMembersByEmail = findGeneralMembersByEmail(email);

        // 일반 회원 계정이 1명이 아닌 경우 예외 처리
        if (generalMembersByEmail.size() != 1) {
            throw new DuplicateMemberException();
        }

        // 1명일 때만 비밀번호 체크 진행
        Member member = generalMembersByEmail.getFirst();
        member.setLastLoginDate(LocalDateTime.now());
        memberRepositoryV1.save(member);
        return member;
    }

    /**
     * 이메일과 비밀번호를 통한 인증
     * - 이메일 인증 여부와 비밀번호를 검증
     *
     * @param email 로그인 이메일
     * @param password 비밀번호
     * @return 인증된 회원 객체
     */
    @Transactional(readOnly = true)
    public Member authenticateMember(String email, String password) {
        Member generalMember = findSingleGeneralMemberByEmail(email);

        if (!generalMember.isCertifyByMail()) {
            throw new BadCredentialsException(ExceptionStatus.AUTHENTICATION_EMAIL_NOT_VERIFIED.getMessage());
        }

        if (!passwordEncoder.matches(password, generalMember.getPassword())) {
            throw new BadCredentialsException(ExceptionStatus.AUTHENTICATION_INVALID_PASSWORD.getMessage());
        }

        return generalMember;
    }

    /**
     * 비밀번호 검증
     * - 이메일로 조회한 회원의 비밀번호가 일치하는지 확인
     *
     * @param email 회원 이메일
     * @param password 입력된 비밀번호
     * @return 비밀번호가 일치하면 true, 그렇지 않으면 false
     */
    @Transactional(readOnly = true)
    public boolean checkPassword(String email, String password) {
        List<Member> generalMembersByEmail = findGeneralMembersByEmail(email);

        // 일반 회원 계정이 1명이 아닌 경우 예외 처리
        if (generalMembersByEmail.size() != 1) {
            throw new DuplicateMemberException();
        }

        // 1명일 때만 비밀번호 체크 진행
        Member member = generalMembersByEmail.getFirst();
        return passwordEncoder.matches(password, member.getPassword());
    }

    /**
     * 이메일 중복 여부 확인
     * - 이미 존재하는 이메일인지 검증
     *
     * @param email 회원 이메일
     */
    @Transactional(readOnly = true)
    public void validateExistedMemberByEmail(String email) {
        if (memberRepositoryV1.existsByEmail(email)) {
            throw new ExistingMemberException();
        }
    }

    /**
     * 이메일 인증 메일 발송
     * - 인증 메일을 회원 이메일로 전송
     *
     * @param email 회원 이메일
     * @param token 인증 토큰
     */
    // 이메일 인증 보내는 서비스는 DB 을 수정하거나 생성하는 트랜젝션이 필요 없어서 readOnly 를 걸었습니다.
    @Transactional(readOnly = true)
    public void sendVerificationEmail(String email, String token) throws Exception {
        memberEmailService.sendEmailVerification(email, token);
    }

    @Transactional(readOnly = true)
    public List<Member> findMembersByEmail(String email) {

        return memberRepository.findAllByEmail(email);
    }


    /**
     * 일반 회원 조회 (LoginType.GENERAL)
     *
     * @param email 회원 이메일
     * @return 일반 회원 리스트
     */
    @Transactional(readOnly = true)
    public List<Member> findGeneralMembersByEmail(String email) {
        // 이메일로 모든 회원을 조회한 후, LoginType이 GENERAL인 회원만 필터링하여 반환
        return memberRepository.findAllByEmail(email)
                .stream()
                .filter(member -> member.getLoginType() == LoginType.GENERAL)
                .collect(Collectors.toList());
    }

    /**
     * 단일 일반 회원 조회
     *
     * @param email 회원 이메일
     * @return 일반 회원 객체
     * @throws EmailNotFoundException 이메일이 존재하지 않을 경우
     * @throws DuplicateMemberException 일반 회원이 1명 이상일 경우
     */
    public Member findSingleGeneralMemberByEmail(String email) {
        List<Member> generalMembers = findGeneralMembersByEmail(email);

        if (generalMembers.isEmpty()) {
            throw new EmailNotFoundException();
        } else if (generalMembers.size() > 1) {
            throw new DuplicateMemberException();
        }

        return generalMembers.getFirst();
    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원의 MemberRole을 업데이트
     *
     * @param requestDto 요청 DTO
     * @return 업데이트된 Member 엔티티
     */
    public Member updateMemberRole(MemberResponseDto requestDto, UUID memberId) {
        // 회원 조회
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.MEMBER_NOT_FOUND));

        // 역할 업데이트
        member.updateRole(requestDto.getRole());

        return memberRepository.save(member);
    }

    @Transactional
    public String memberLogout(String refreshAuthorization, HttpServletRequest request, HttpServletResponse response) {

        // RefreshAuthorization 검증
        String refreshToken = extractRefreshToken(refreshAuthorization);
        if (refreshToken == null) {
            throw new IllegalArgumentException("Invalid or missing refreshAuthorization cookie");
        }

        // Refresh Token 유효성 검증 및 삭제 처리
        Optional<Refresh> optionalRefresh = refreshRepository.findByRefreshToken(refreshToken);
        if (optionalRefresh.isEmpty()) {
            throw new IllegalArgumentException("Invalid Refresh Token");
        }

        Refresh refreshEntity = optionalRefresh.get();
        Member member = memberRepositoryV1.findById(refreshEntity.getMember().getMemberId())
                .orElseThrow(() -> new UsernameNotFoundException("Member not found for ID: " + refreshEntity.getMember().getMemberId()));

        // 쿠키 삭제
        WebCookieUtil.deleteCookie(response, "refreshAuthorization");

        // Refresh Token 삭제
        refreshRepository.delete(refreshEntity);

        log.info("Member ID: {} has logged out.", member.getMemberId());

        // 메시지만 반환
        return "Member ID: " + member.getMemberId() + " has logged out.";
    }

    private String extractRefreshToken(String refreshAuthorization) {
        if (refreshAuthorization == null || refreshAuthorization.isEmpty()) {
            return null;
        }

        if (!refreshAuthorization.startsWith("Bearer+")) {
            return null;
        }

        String refreshToken = refreshAuthorization.substring(7);
        return refreshToken.isEmpty() ? null : refreshToken;
    }


    // 사용자 정의 예외 클래스
    public static class ExistingMemberException extends ServiceException {
        public ExistingMemberException() {
            super(ExceptionStatus.EXISTING_MEMBER_EMAIL);
        }
    }
    public static class EmailNotFoundException extends ServiceException {
        public EmailNotFoundException() {
            super(ExceptionStatus.AUTHENTICATION_NOT_FOUND_EMAIL);
        }
    }
    public static class DuplicateMemberException extends ServiceException {
        public DuplicateMemberException() {
            super(ExceptionStatus.DUPLICATE_MEMBER);
        }
    }
}
