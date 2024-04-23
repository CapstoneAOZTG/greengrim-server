package com.greengrim.green.core.chatroom.controller;

import com.greengrim.green.core.chatroom.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/chatrooms")
public class ChatroomController {

  private final ChatroomService chatroomService;

}
