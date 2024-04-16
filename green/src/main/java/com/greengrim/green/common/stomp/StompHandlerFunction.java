package com.greengrim.green.common.stomp;

import com.greengrim.green.common.oauth.jwt.JwtTokenProvider;
import com.greengrim.green.core.chat.service.ChatService;
import com.greengrim.green.core.chatroom.service.ChatroomRedisService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandlerFunction {

  private final JwtTokenProvider jwtTokenProvider;
  private final ChatService chatService;
  private final ChatroomRedisService chatroomRedisService;

  public void connect(StompHeaderAccessor accessor, Message<?> message) {
    String jwtToken = accessor.getFirstNativeHeader("token");
    String sessionId = (String) message.getHeaders().get("simpSessionId");
    jwtTokenProvider.validateAccessToken(jwtToken);
    log.info("CONNECT {}", sessionId);
  }

  public void subscribe(Message<?> message) {
    String roomId = chatService.getRoomId(Optional.ofNullable((String) message.getHeaders()
        .get("simpDestination")).orElse("0"));
    String sessionId = (String) message.getHeaders().get("simpSessionId");
    chatroomRedisService.subscribe(sessionId, roomId);
    log.info("SUBSCRIBED {}, {}", sessionId, roomId);
  }

  public void unsubscribe(Message<?> message) {
    String sessionId = (String) message.getHeaders().get("simpSessionId");
    chatroomRedisService.unsubscribe(sessionId);
  }

  public void disconnect(StompHeaderAccessor accessor, Message<?> message) {
    String jwtToken = accessor.getFirstNativeHeader("token");
    String sessionId = (String) message.getHeaders().get("simpSessionId");
    jwtTokenProvider.validateAccessToken(jwtToken);

    Long memberId = Long.valueOf(jwtTokenProvider.getMemberIdByAccessToken(jwtToken));
    chatroomRedisService.unsubscribeAll(sessionId, memberId);
    log.info("DISCONNECT {}", memberId);
  }
}
