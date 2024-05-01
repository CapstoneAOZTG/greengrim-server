package com.greengrim.green.core.chat.service;

import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.chat.ChatMessage;
import com.greengrim.green.core.chat.ChatMessage.MessageType;
import com.greengrim.green.core.chat.LastChatStorage;
import com.greengrim.green.core.chat.repository.ChatRepository;
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

  public void checkLastMessage(ChatMessage chatMessage) {
    ChatMessage lastChatMessage = lastChatStorage.getMessage(chatMessage.getRoomId());

    if (lastChatMessage != null) {
      // 보내려는 메세지와 가장 최근 메세지의 송신자와 보낸 시간(분)이 일치한다면
      if (chatMessage.getSenderId().equals(lastChatMessage.getSenderId())
          && chatMessage.getCreatedAt().substring(0, 11).equals(lastChatMessage.getCreatedAt().substring(0, 11))) {
        chatMessage.setProfileImg("");
        chatMessage.setSentTime("");
      }
    }
    lastChatStorage.setMessage(chatMessage.getRoomId(), chatMessage);
  }

  public void deleteMember(Member member) {
    chatRepository.updateProfileAndNicknameBySenderId(member.getId(), member.getProfileBasicImgUrl(), "알수없음");
  }

}
