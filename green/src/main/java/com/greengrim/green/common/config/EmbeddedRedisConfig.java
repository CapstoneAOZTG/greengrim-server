package com.greengrim.green.common.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

@Profile("temp") // 일시적으로 local 해제
@Configuration
public class EmbeddedRedisConfig {

  @Value("${spring.data.redis.port}")
  private int redisPort;
  private RedisServer redisServer;

  @PostConstruct
  public void redisServer() {
    redisServer = new RedisServer(redisPort);
    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {
    if(redisServer != null) {
      redisServer.stop();
    }
  }
}