package com.greengrim.green.core.chat.repository;

import com.greengrim.green.core.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {

  Page<ChatMessage> findByRoomId(Long roomId, Pageable pageable);

  ChatMessage findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

  ChatMessage findChatMessageBySenderIdAndCreatedAt(Long senderId, String createdAt);

  Long countByRoomIdAndCreatedAtGreaterThanAndType(Long roomId, String createdAt, String type);

  void deleteBySenderIdAndCreatedAt(Long senderId, String createdAt);

  @Modifying
  @Query("update chatMessage c set c.profileImg = :basicUrl, c.nickName = :basicNickName where c.senderId = :senderId")
  void updateProfileAndNicknameBySenderId(@Param("senderId") Long senderId,
                                          @Param("basicUrl") String basicUrl,
                                          @Param("basicNickName") String basicNickName);

}
