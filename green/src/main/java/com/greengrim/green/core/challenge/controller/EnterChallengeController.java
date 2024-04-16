package com.greengrim.green.core.challenge.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.EnterChallengeInfo;
import com.greengrim.green.core.challenge.service.EnterChallengeService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/challenges")
public class EnterChallengeController {

  private final EnterChallengeService enterChallengeService;

  /**
   * [POST] 챌린지 참가 - 채팅방 입장
   */
  @Operation(summary = "챌린지 참가 - 채팅방 입장")
  @PostMapping("/enter")
  public ResponseEntity<EnterChallengeInfo> enterChallenge(@CurrentMember Member member, @RequestParam Long id) {
    return ResponseEntity.ok(enterChallengeService.enterChallenge(member, id));
  }

  /**
   * [POST] 챌린지 포기 - 채팅방 퇴장
   */
  @Operation(summary = "챌린지 포기 - 채팅방 퇴장")
  @PostMapping("/exit")
  public void exitChallenge(@CurrentMember Member member, @RequestParam Long id) {
    enterChallengeService.exitChallenge(member, id);
  }

}
