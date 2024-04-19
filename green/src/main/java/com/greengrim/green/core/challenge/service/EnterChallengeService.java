package com.greengrim.green.core.challenge.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.EnterChallengeInfo;
import com.greengrim.green.core.challenge.repository.ChallengeRepository;
import com.greengrim.green.core.chatroom.service.ChatroomService;
import com.greengrim.green.core.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnterChallengeService {

  private final ChallengeRepository challengeRepository;

  private final GetChallengeService getChallengeService;
  private final ChatroomService chatroomService;


  /**
   * 챌린지 참가 - 채팅방 입장
   */
  public EnterChallengeInfo enterChallenge(Member member, Long id) {
    Challenge challenge = getChallengeService.findByIdWithValidation(id);

    if (challenge.getCapacity() == challenge.getHeadCount())
      throw new BaseException(ChallengeErrorCode.OVER_CAPACITY_CHALLENGE);

    if(chatroomService.isMemberEntered(member.getId(), challenge.getChatroom().getId()))
      throw new BaseException(ChallengeErrorCode.ALREADY_ENTERED_CHALLENGE);

    challenge.setHeadCount(challenge.getHeadCount() + 1);
    challengeRepository.save(challenge);
    chatroomService.enterChatroom(member, challenge.getChatroom());
    return new EnterChallengeInfo(challenge);
  }

  /**
   * 챌린지 포기 - 채팅방 퇴장
   */
  public void exitChallenge(Member member, Long id) {
    Challenge challenge = getChallengeService.findByIdWithValidation(id);
    challenge.setHeadCount(challenge.getHeadCount() - 1);
    challengeRepository.save(challenge);
    chatroomService.exitChatroom(member, challenge.getChatroom().getId());

    if(challenge.getHeadCount() == 0)
      chatroomService.removeChatroom(challenge.getChatroom());
  }
}
