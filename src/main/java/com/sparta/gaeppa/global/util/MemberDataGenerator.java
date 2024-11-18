package com.sparta.gaeppa.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dummy")
@Order(1) // 다른 순서 지정
@RequiredArgsConstructor
public class MemberDataGenerator implements ApplicationRunner {

    private final MemberDataUtil memberDataUtil;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberDataUtil.generateMemberData();
    }
}
