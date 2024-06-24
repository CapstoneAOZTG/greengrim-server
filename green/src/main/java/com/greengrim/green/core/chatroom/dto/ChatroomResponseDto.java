package com.greengrim.green.core.chatroom.dto;

import com.greengrim.green.core.chatroom.entity.Chatroom;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ChatroomResponseDto {

  @Getter
  @RequiredArgsConstructor
  public static class ChatroomInfo {
    private Long id;
    private String title;
    private boolean status;

    public ChatroomInfo(Chatroom chatroom) {
      this.id = chatroom.getId();
      this.title = chatroom.getTitle();
      this.status = chatroom.isStatus();
    }
  }
}
