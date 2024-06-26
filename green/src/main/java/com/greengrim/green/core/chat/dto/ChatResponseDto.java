package com.greengrim.green.core.chat.dto;

import com.greengrim.green.core.chat.entity.ChatMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChatResponseDto {

    @Getter
    @AllArgsConstructor
    public static class MessageInfos {
        private List<ChatMessage> messages;
    }

}
