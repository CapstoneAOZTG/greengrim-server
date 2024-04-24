package com.greengrim.green.core.chat.repository;

import com.greengrim.green.core.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {

  Page<ChatMessage> findByRoomId(Long roomId, Pageable pageable);

  ChatMessage findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

  Long countByRoomIdAndCreatedAtGreaterThanAndType(Long roomId, String createdAt, String type);

}
