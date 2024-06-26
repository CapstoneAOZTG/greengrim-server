package com.greengrim.green.core.chat.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.chat.entity.ChatMessage;
import com.greengrim.green.core.chat.entity.ChatMessage.MessageType;
import com.greengrim.green.core.chat.dto.ChatResponseDto.MessageInfos;
import com.greengrim.green.core.chat.repository.ChatRepository;
import com.greengrim.green.core.chatroom.entity.Chatroom;
import com.greengrim.green.core.chatroom.repository.ChatroomRepository;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.member.service.GetMemberService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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

    redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    fcmService.sendChatMessage(chatMessage);
    chatRepository.save(chatMessage);
  }

  public MessageInfos getMessages(Long roomId, String createdAt) {
    List<ChatMessage> messages;
    if(createdAt.equals("0"))
      messages = chatRepository.findTop100ByRoomIdOrderByCreatedAtDesc(roomId);
    else
      messages = chatRepository.findTop100ByRoomIdAndCreatedAtLessThanOrderByCreatedAtDesc(roomId, createdAt);

    for (ChatMessage message : messages) {
      if (!message.isChild()) {
        messages = messages.subList(messages.indexOf(message), messages.size());
        break;
      }
    }

    return new MessageInfos(messages);
  }

  @Scheduled(cron = "0 0 0 * * ?")
  public void sendDateMessage() {
    ChatMessage chatMessage = new ChatMessage();

    List<Chatroom> chatrooms = chatroomRepository.findAllByStatusIsTrue();
    for(Chatroom chatroom : chatrooms) {
      chatMessage.setDateMessage(chatroom.getId());
      redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
      chatRepository.save(chatMessage);
    }
  }
 }
