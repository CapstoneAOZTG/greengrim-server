package com.greengrim.green.core.chat.repository;

import com.greengrim.green.core.chat.ChatMessage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {

  Page<ChatMessage> findByRoomId(Long roomId, Pageable pageable);

  Optional<ChatMessage> findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

  Long countByRoomIdAndCreatedAtGreaterThan(Long roomId, String createdAt);
}
