package com.greengrim.green.core.challenge.controller;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto.RegisterChallenge;
import com.greengrim.green.core.challenge.dto.ChallengeRequestDto.SearchRequest;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.ChallengeSimpleInfo;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.EnterChallengeResponse;
import com.greengrim.green.core.challenge.service.GetChallengeService;
import com.greengrim.green.core.challenge.service.RegisterChallengeService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterChallengeController {

    private final RegisterChallengeService registerChallengeService;
    private final GetChallengeService getChallengeService;

    /**
     * [POST] 챌린지 생성
     */
    @Operation(summary = "챌린지 생성")
    @PostMapping("/visitor/challenges")
    public ResponseEntity<EnterChallengeResponse> registerChallenge(@CurrentMember Member member,
            @Valid @RequestBody RegisterChallenge registerChallenge) {
        return ResponseEntity.ok(registerChallengeService.register(member, registerChallenge));
    }

    /**
     * [POST] 챌린지 검색
     */
    @Operation(summary = "챌린지 검색")
    @PostMapping("/visitor/challenges/searches")
    public ResponseEntity<PageResponseDto<List<ChallengeSimpleInfo>>> searchChallenges(
            @CurrentMember Member member,
            @RequestParam(value = "category", required = false) Category category,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(getChallengeService.searchChallenges(
                member, category, page, size, searchRequest.getKeyword()));
    }
}
