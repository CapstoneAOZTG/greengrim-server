package com.greengrim.green.core.chat.controller;

import com.greengrim.green.core.chat.entity.ChatMessage;
import com.greengrim.green.core.chat.dto.ChatResponseDto.MessageInfos;
import com.greengrim.green.core.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "채팅")
public class ChatController {

  private final ChatService chatService;

  /**
   * [MessageMapping] 메세지 전송
   * Todo: @CurrentMember 헤더를 사용할 수 있다면 추가
   */
  @Operation(summary = "메세지 전송")
  @MessageMapping("/chat/message")
  public void message(ChatMessage message) {
    chatService.sendChatMessage(message);
  }

  @Operation(summary = "메세지 조회")
  @GetMapping("/chat/message")
  public ResponseEntity<MessageInfos> getMessage(
      @RequestParam(value = "roomId") Long roomId,
      @RequestParam(value = "createdAt") String createdAt) {
    return ResponseEntity.ok(chatService.getMessages(roomId, createdAt));
  }
}
