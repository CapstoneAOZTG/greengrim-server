package com.greengrim.green.core.chatroom.service;

import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chat.ChatMessage.MessageType;
import com.greengrim.green.core.chat.service.ChatService;
import com.greengrim.green.core.chatparticipant.ChatparticipantService;
import com.greengrim.green.core.chatroom.Chatroom;
import com.greengrim.green.core.chatroom.repository.ChatroomRepository;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChatroomService{

  private final ChatroomRepository chatroomRepository;

  private final ChatroomRedisService chatroomRedisService;
  private final ChatparticipantService chatparticipantService;
  private final ChatService chatService;

  /**
   * 채팅방 생성
   */
  @Transactional
  public Chatroom registerChatroom(Member member, String title) {
    Chatroom chatroom = Chatroom.builder()
        .title(title)
        .status(true)
        .build();
    chatroomRepository.save(chatroom);
    chatroomRedisService.createChatroom(chatroom);
    return chatroom;
  }

  /**
   * 채팅방 삭제
   */
  @Transactional
  public void removeChatroom(Chatroom chatroom) {
    chatroom.setStatus(false);
    chatroomRepository.save(chatroom);
    chatroomRedisService.removeChatroom(String.valueOf(chatroom.getId()));
  }

  /**
   * 채팅방 입장
   */
  @Transactional
  public void enterChatroom(Member member, Chatroom chatroom) {
    chatparticipantService.save(member, chatroom);
    chatService.sendChatMessage(ChatMessage.builder()
        .type(MessageType.ENTER)
        .roomId(chatroom.getId())
        .senderId(member.getId())
        .message(member.getNickName() + "님이 입장했습니다.")
        .isChild(false)
        .build());
    log.info("SUBSCRIBED {}, {}", member.getNickName(), chatroom.getId());
  }

  /**
   * 채팅방 퇴장
   */
  @Transactional
  public void exitChatroom(Member member, Long chatroomId) {
    chatparticipantService.remove(member.getId(), chatroomId);
    chatService.sendChatMessage(ChatMessage.builder()
        .type(MessageType.EXIT)
        .roomId(chatroomId)
        .senderId(member.getId())
        .message(member.getNickName() + "님이 퇴장했습니다.")
        .build());
    log.info("DISCONNECTED {}, {}", member.getNickName(), chatroomId);
  }

  public boolean isMemberEntered(Long memberId, Long chatRoomId) {
    if (chatparticipantService.
        checkParticipantExists(memberId, chatRoomId)) {
      return true;
    }

    return false;
  }
}
