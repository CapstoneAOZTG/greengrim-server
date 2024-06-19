package com.greengrim.green.core.chat.repository;

import com.greengrim.green.core.chat.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ChatRepository extends MongoRepository<ChatMessage, String> {

  List<ChatMessage> findTop100ByRoomIdAndCreatedAtLessThanOrderByCreatedAtDesc(Long roomId, String createdAt);

  List<ChatMessage> findTop100ByRoomIdOrderByCreatedAtDesc(Long roomId);

  boolean existsByRoomIdAndCreatedAtLessThan(Long roomId, String specificDate);

  ChatMessage findFirstByRoomIdOrderByCreatedAtDesc(Long roomId);

  Long countByRoomIdAndCreatedAtGreaterThanAndType(Long roomId, String createdAt, String type);

  @Modifying
  @Query("update chatMessage c set c.profileImg = :basicUrl, c.nickName = :basicNickName where c.senderId = :senderId")
  void updateProfileAndNicknameBySenderId(@Param("senderId") Long senderId,
                                          @Param("basicUrl") String basicUrl,
                                          @Param("basicNickName") String basicNickName);
}
