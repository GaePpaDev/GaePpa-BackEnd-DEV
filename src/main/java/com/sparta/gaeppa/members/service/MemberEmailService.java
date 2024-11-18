package com.sparta.gaeppa.members.service;

import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.repository.MemberRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberEmailService {

    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;

    @Value("${backend.url}")
    private String backendUrl;

    @Transactional(readOnly = true)
    public void sendEmailVerification(String email, String emailToken) throws Exception {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email); // 받는 분 메일 추가
        message.setSubject("개빠른 민족 회원가입 이메일 인증입니다. "); // 제목

        String body = "<div>"
                + "<h1> 안녕하세요. 개빠른 민족 입니다.!</h1>"
                + "<br>"
                + "<p> 개빠른 민족 회원가입을 축하드리며, 저희 서비스를 이용해주셔서 감사합니다. <p>"
                + "<p> 아래 링크를 클릭하면 이메일 인증이 완료됩니다.<p>"
                + "<a href='" + backendUrl + "/auth/verify?token=" + emailToken + "'>인증 링크</a>"
                + "</div>";
        message.setText(body, "utf-8", "html");// 내용, charset 타입, subtype
        message.setFrom(new InternetAddress("gokorea1214@naver.com", "CORE_ADMIN")); // 보내는 사람
        javaMailSender.send(message);
    }

    @Transactional
    public Member updateByVerifyToken(String emailToken) {
        Optional<Member> optionalMember = memberRepository.findByEmailToken(emailToken);

        // 검색된 회원이 있을 경우, 업데이트 수행.
        if (optionalMember.isPresent()) {
            // 회원 정보를 업데이트합니다.
            Member member = optionalMember.get();
            // 회원의 이메일 인증 여부를 True 로 반환.
            member.certifyEmail();
            // 변경된 이메일 인증 여부, 이메일 토큰을 DB에 반영.
            return memberRepository.save(member);
        } else {
            throw new UsernameNotFoundException("해당 토큰을 가진 멤버가 존재하지 않습니다!");
        }
    }
}
