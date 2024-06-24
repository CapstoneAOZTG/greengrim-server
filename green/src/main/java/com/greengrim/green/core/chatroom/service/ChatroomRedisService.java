package com.greengrim.green.core.chatroom.service;

import com.greengrim.green.core.chatparticipant.entity.Chatparticipant;
import com.greengrim.green.core.chatparticipant.service.ChatparticipantService;
import com.greengrim.green.core.chatroom.entity.Chatroom;
import com.greengrim.green.core.chatroom.repository.ChatroomRedisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatroomRedisService {

  private final ChatroomRedisRepository chatroomRedisRepository;

  private final ChatparticipantService chatparticipantService;

  /**
   * 레디스 서버 채팅방 등록
   */
  public void createChatroom(Chatroom chatroom) {
    chatroomRedisRepository.create(chatroom);
  }

  /**
   * 레디스 서버 채팅방 삭제
   */
  public void removeChatroom(String roomId) {
    chatroomRedisRepository.remove(roomId);
  }

  /**
   * 채팅방 구독
   */
  public void subscribe(String sessionId, String roomId) {
    chatroomRedisRepository.subscribe(sessionId, roomId);
  }

  /**
   * 채팅방 구독 해제
   */
  public void unsubscribe(String sessionId) {
    chatroomRedisRepository.unsubscribe(sessionId);
  }

  /**
   * 멤버가 가지고 있는 전체 채팅방 구독 해제
   */
  public void unsubscribeAll(String sessionId, Long memberId) {
    List<Chatparticipant> chatparticipants = chatparticipantService.findByMemberId(memberId);
    chatparticipants.forEach(chatparticipant ->
        chatroomRedisRepository.unsubscribe(sessionId));
  }
}
