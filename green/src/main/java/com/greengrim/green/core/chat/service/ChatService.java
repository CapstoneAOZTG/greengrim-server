package com.greengrim.green.core.chat.service;

import static com.greengrim.green.common.constants.ServerConstants.BASIC_PROFILE_IMG_URL;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chat.ChatMessage.MessageType;
import com.greengrim.green.core.chat.LastChatStorage;
import com.greengrim.green.core.chat.repository.ChatRepository;
import com.greengrim.green.core.chatroom.Chatroom;
import com.greengrim.green.core.chatroom.repository.ChatroomRepository;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.service.GetMemberService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChannelTopic channelTopic;
  private final RedisTemplate redisTemplate;

  private final GetMemberService getMemberService;
  private final FcmService fcmService;
  private final ChatRepository chatRepository;

  private final LastChatStorage lastChatStorage;
  private final ChatroomRepository chatroomRepository;

  public String getRoomId(String destination) {
    int lastIndex = destination.lastIndexOf('/');
    if (lastIndex != -1)
      return destination.substring(lastIndex + 1);
    else
      return "";
  }

  @Transactional
  public void sendChatMessage(ChatMessage chatMessage) {
    chatMessage.setTime();

    // CERT 타입이 아닐 떄
    if(!MessageType.CERT.equals(chatMessage.getType())) {
      chatMessage.setCertId((long) -1);
      chatMessage.setCertImg("");
    }
    // ENTER, QUIT 타입일 때
    if(MessageType.ENTER.equals(chatMessage.getType()) ||
        MessageType.EXIT.equals(chatMessage.getType())) {
      chatMessage.setNickName("");
      chatMessage.setProfileImg("");
    }
    // ENTER, QUIT 타입이 아닐 때
    else {
      Optional<Member> member = getMemberService.findMemberById(chatMessage.getSenderId());
      chatMessage.setNickName(member.get().getNickName());
      chatMessage.setProfileImg(member.get().getProfileImgUrl());
    }

    checkLastMessage(chatMessage);

  }

  public PageResponseDto<List<ChatMessage>> getMessages(Long roomId, int page, int size) {
    Page<ChatMessage> messages = chatRepository.findByRoomId(roomId, getPageable(page, size,SortOption.DESC));
    return pagingChatMessage(messages);
  }

  private PageResponseDto<List<ChatMessage>> pagingChatMessage(Page<ChatMessage> chatMessages) {
    List<ChatMessage> messages = new ArrayList<>();
    chatMessages.forEach(message -> messages.add(message));
    return new PageResponseDto<>(chatMessages.getNumber(), chatMessages.hasNext(), messages);
  }

  public void checkLastMessage(ChatMessage chatMessage) {
    boolean isDuplicated = false;
    ChatMessage lastChatMessage = lastChatStorage.getMessage(chatMessage.getRoomId());

    // 채팅방에 메세지가 없거나
    // 보내려는 메세지 또는 최근 메세지가 DATE 타입이 아니라면 검사
    if (lastChatMessage != null || lastChatMessage.getType() != MessageType.DATE ||
        chatMessage.getType() != MessageType.DATE ) {

      // 최근 메세지와 보내려는 메세지의 송신자가 같다면
      if (chatMessage.getSenderId().equals(lastChatMessage.getSenderId())
          && chatMessage.getCreatedAt().substring(0, 12).equals(lastChatMessage.getCreatedAt().substring(0, 12))) {
        isDuplicated = true;
        chatMessage.setProfileImg("");
      }
    }

    // 메시지 전송
    redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    fcmService.sendChatMessage(chatMessage);
    lastChatStorage.setMessage(chatMessage.getRoomId(), chatMessage);

    if(isDuplicated) {
      ChatMessage updateMessage = chatRepository.findChatMessageBySenderIdAndCreatedAt(
          lastChatMessage.getSenderId(), lastChatMessage.getCreatedAt());
      updateMessage.setSentTime("");
      chatRepository.deleteBySenderIdAndCreatedAt(lastChatMessage.getSenderId(),
          lastChatMessage.getCreatedAt());
      chatRepository.save(updateMessage);
    }
  }


  @Scheduled(cron = "0 0 0 * * ?")
  public void sendDateMessage() {
    ChatMessage chatMessage = new ChatMessage();

    List<Chatroom> chatrooms = chatroomRepository.findAll();
    for(Chatroom chatroom : chatrooms) {
      chatMessage.setDateMessage(chatroom.getId());
      redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
  }

  public void deleteMember(Member member) {
    chatRepository.updateProfileAndNicknameBySenderId(member.getId(), BASIC_PROFILE_IMG_URL, "알수없음");
  }
}
