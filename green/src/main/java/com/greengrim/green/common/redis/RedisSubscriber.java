package com.greengrim.green.common.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengrim.green.core.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

  private final ObjectMapper objectMapper;
  private final SimpMessageSendingOperations messagingTemplate;

  public void sendMessage(String publishMessage) {
    try{
      ChatMessage chatMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
      messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

}
