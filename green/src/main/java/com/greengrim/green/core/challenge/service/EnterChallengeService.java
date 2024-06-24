package com.greengrim.green.core.challenge.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.ChallengeErrorCode;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.challenge.dto.ChallengeResponseDto.EnterChallengeInfo;
import com.greengrim.green.core.chat.repository.ChatRepository;
import com.greengrim.green.core.chatroom.service.ChatroomService;
import com.greengrim.green.core.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnterChallengeService {

  private final GetChallengeService getChallengeService;
  private final ChatroomService chatroomService;
  private final FcmService fcmService;
  private final UpdateChallengeService updateChallengeService;

  private final ChatRepository chatRepository;

  /**
   * 챌린지 참가 - 채팅방 입장
   */
  @Transactional
  public EnterChallengeInfo enterChallenge(Member member, Long id) {
    Challenge challenge = getChallengeService.findByIdWithValidation(id);

    if (challenge.getCapacity() == challenge.getHeadCount())
      throw new BaseException(ChallengeErrorCode.OVER_CAPACITY_CHALLENGE);

    if(chatroomService.isMemberEntered(member.getId(), challenge.getChatroom().getId()))
      throw new BaseException(ChallengeErrorCode.ALREADY_ENTERED_CHALLENGE);

    challenge.setHeadCount(challenge.getHeadCount() + 1);
    chatroomService.enterChatroom(member, challenge.getChatroom());
    fcmService.subscribeChatroom(member, challenge.getChatroom().getId());

    return new EnterChallengeInfo(challenge);
  }

  /**
   * 챌린지 포기 - 채팅방 퇴장
   */
  @Transactional
  public void exitChallenge(Member member, Long id) {
    Challenge challenge = getChallengeService.findByIdWithValidation(id);
    challenge.setHeadCount(challenge.getHeadCount() - 1);

    chatroomService.exitChatroom(member, challenge.getChatroom().getId());
    fcmService.unsubscribeChatroom(member, challenge.getChatroom().getId());

    if(challenge.getHeadCount() == 0)
      // 채팅방 삭제
      chatroomService.removeChatroom(challenge.getChatroom());
      // 채팅 메시지 삭제
      chatRepository.deleteByRoomId(challenge.getChatroom().getId());
      // 챌린지, 인증 삭제
      updateChallengeService.deleteChallengeAndCertification(challenge);
  }
}
