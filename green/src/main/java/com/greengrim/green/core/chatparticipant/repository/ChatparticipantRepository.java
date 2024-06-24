package com.greengrim.green.core.chatparticipant.repository;

import java.util.List;

import com.greengrim.green.core.chatparticipant.entity.Chatparticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatparticipantRepository extends JpaRepository<Chatparticipant, Long> {

  List<Chatparticipant> findByMemberId(Long memberId);
  Chatparticipant findByMemberIdAndChatroomId(Long memberId, Long chatroomId);
  boolean existsByMemberIdAndChatroomId(Long memberId, Long chatroomId);
}
