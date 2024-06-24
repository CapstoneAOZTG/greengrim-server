package com.greengrim.green.core.chat.repository;

import com.greengrim.green.core.chat.entity.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {

  List<ChatMessage> findTop100ByRoomIdAndCreatedAtLessThanOrderByCreatedAtDesc(Long roomId, String createdAt);

  List<ChatMessage> findTop100ByRoomIdOrderByCreatedAtDesc(Long roomId);

  ChatMessage findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

  Long countByRoomIdAndCreatedAtGreaterThanAndType(Long roomId, String createdAt, String type);

  void deleteByRoomId(Long id);
}
