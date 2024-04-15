package com.greengrim.green.core.chatroom.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.chatroom.service.ChatroomService;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/chatrooms")
public class ChatroomController {

  private final ChatroomService chatroomService;

  /**
   * [POST] 채팅방 퇴장
   */
  @Operation(summary = "채팅방 퇴장")
  @PostMapping("/exit")
  public void exitChatroom(@CurrentMember Member member, @RequestParam Long chatroomId) {
    chatroomService.exitChatroom(member, chatroomId);
  }

}
