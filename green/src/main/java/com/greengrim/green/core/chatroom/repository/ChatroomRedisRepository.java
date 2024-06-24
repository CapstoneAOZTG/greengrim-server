package com.greengrim.green.core.chatroom.repository;

import com.greengrim.green.core.chatroom.entity.Chatroom;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatroomRedisRepository {

  private static final String CHAT_ROOMS = "CHAT_ROOM";
  private static final String ENTER_INFO = "ENTER_INFO";
  @Resource(name = "redisTemplate")
  private HashOperations<String, String, Chatroom> hashOpsChatroom;
  @Resource(name = "redisTemplate")
  private HashOperations<String, String, String> hashOpsEnterInfo;

  public void create(Chatroom chatroom) {
    hashOpsChatroom.put(CHAT_ROOMS, String.valueOf(chatroom.getId()), chatroom);
  }

  public void remove(String roomId) {
    hashOpsChatroom.delete(CHAT_ROOMS, roomId);
  }

  public void subscribe(String sessionId, String roomId) {
    hashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
  }

  public void unsubscribe(String sessionId) {
    hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
  }

  public String getUserEnterRoomId(String sessionId) {
    return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
  }

  public List<Chatroom> findAllRoom() {
    return hashOpsChatroom.values(CHAT_ROOMS);
  }

  public Chatroom findRoomById(Long id) {
    return hashOpsChatroom.get(CHAT_ROOMS, id);
  }
}
