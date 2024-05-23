package com.greengrim.green.core.hiding.challenge;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/hiding")
public class ChallengeHidingController {

    private final ChallengeHidingService challengeHidingService;

    /**
     * [POST] 챌린지 숨기기
     */
    @Operation(summary = "챌린지 숨기기")
    @PostMapping("/challenge")
    public ResponseEntity<Integer> hideChallenge(
            @CurrentMember Member member,
            @RequestParam("id") Long id) {
        challengeHidingService.hideChallenge(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
