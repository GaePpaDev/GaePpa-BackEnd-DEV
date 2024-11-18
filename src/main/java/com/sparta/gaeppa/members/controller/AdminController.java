package com.sparta.gaeppa.members.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.members.dto.admin.MemberResponseDto;
import com.sparta.gaeppa.members.entity.Member;
import com.sparta.gaeppa.members.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admins")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final MemberService memberService;

    /**
     * 회원의 MemberRole을 수정
     *
     * @param requestDto
     * @return ResponseEntity<ApiResult<JoinResponseDto>>
     */
    @PutMapping("/members/role/{memberID}")
    public ResponseEntity<ApiResult<MemberResponseDto>> updateMemberRole(
            @RequestBody @Valid MemberResponseDto requestDto, @PathVariable UUID memberID) {

        // 회원의 역할(Role) 업데이트
        Member updatedMember = memberService.updateMemberRole(requestDto, memberID);

        // DTO로 변환 후 반환
        MemberResponseDto responseDto = MemberResponseDto.fromEntity(updatedMember);
        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResult<List<MemberResponseDto>>> getMembersListByAdmin() {
        // 모든 회원을 조회
        List<Member> allMembers = memberService.findAllMembers();

        // Member 리스트를 MemberResponseDto 리스트로 변환
        List<MemberResponseDto> memberResponseDtos = allMembers.stream()
                .map(MemberResponseDto::fromEntity) // 각 Member를 MemberResponseDto로 변환
                .toList();

        // 결과 반환
        return new ResponseEntity<>(success(memberResponseDtos), HttpStatus.OK);
    }
}
