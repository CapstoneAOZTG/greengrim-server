package com.greengrim.green.core.challenge.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto;
import com.greengrim.green.core.challenge.service.UpdateChallengeService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/challenges")
@Tag(name = "챌린지")
public class UpdateChallengeController {

    private final UpdateChallengeService updateChallengeService;

    /**
     * [PATCH] 챌린지 수정하기
     */
    @Operation(summary = "챌린지 수정하기")
    @PatchMapping("/{id}")
    public ResponseEntity<Integer> modify(
            @Valid @RequestBody ChallengeRequestDto.ModifyChallenge modifyChallenge,
            @PathVariable("id") Long id,
            @CurrentMember Member member) {
        updateChallengeService.modify(member, id, modifyChallenge);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
