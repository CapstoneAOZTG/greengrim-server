package com.greengrim.green.core.hiding.member;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberHidingController {

    private final MemberHidingService memberHidingService;

    /**
     * [POST] Member 숨기기
     */
    @Operation(summary = "Member 숨기기")
    @PostMapping("/visitor/hiding/member")
    public ResponseEntity<Integer> hideMember(
            @CurrentMember Member member,
            @RequestParam("id") Long id) {
        memberHidingService.hideMember(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
