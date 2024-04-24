package com.greengrim.green.core.chat.service;

import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chat.ChatMessage.MessageType;
import com.greengrim.green.core.chat.repository.ChatRepository;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.service.GetMemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChannelTopic channelTopic;
  private final RedisTemplate redisTemplate;

  private final GetMemberService getMemberService;
  private final FcmService fcmService;
  private final ChatRepository chatRepository;

  public String getRoomId(String destination) {
    int lastIndex = destination.lastIndexOf('/');
    if (lastIndex != -1)
      return destination.substring(lastIndex + 1);
    else
      return "";
  }

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
    else {

      // TIME 일 떄
      if (MessageType.DATE.equals(chatMessage.getType())) {
        chatMessage.setNickName("");
        chatMessage.setProfileImg("");
        chatMessage.setMessage(chatMessage.getCreatedAt());
      } else {
        Optional<Member> member = getMemberService.findMemberById(chatMessage.getSenderId());
        chatMessage.setNickName(member.get().getNickName());
        chatMessage.setProfileImg(member.get().getProfileImgUrl());
      }
    }
    redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    fcmService.sendChatMessage(chatMessage);
    chatRepository.save(chatMessage);
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

}
