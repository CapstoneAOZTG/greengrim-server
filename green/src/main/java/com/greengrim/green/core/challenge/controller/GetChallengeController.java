package com.greengrim.green.core.challenge.controller;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeDetailInfo;
import com.greengrim.green.core.challenge.service.GetChallengeService;
import com.greengrim.green.core.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetChallengeController {

    private final GetChallengeService getChallengeService;

    /**
     * [GET] 챌린지 상세 조회
     */
    @GetMapping("/challenges/{id}")
    public ResponseEntity<ChallengeDetailInfo> getChallengeInfo(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
                getChallengeService.getChallengeDetail(member, id),
                HttpStatus.OK);
    }
}
