package com.greengrim.green.common.config;

import com.greengrim.green.common.stomp.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

  private final StompHandler stompHandler;

  @Override
  public void configureClientInboundChannel(ChannelRegistration registry) {
    registry.interceptors(stompHandler);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*");
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    long[] heartbeat = { 30000, 30000 };
    config.enableSimpleBroker("/sub")
        .setHeartbeatValue(heartbeat)
        .setTaskScheduler(new DefaultManagedTaskScheduler());
    config.setApplicationDestinationPrefixes("/pub");
  }

}
