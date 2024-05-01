package com.greengrim.green.core.chat;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LastChatStorage {

    private static Map<Long, ChatMessage> lastChatMessages = new HashMap<>();

    public static ChatMessage getMessage(Long roomId) {
        return lastChatMessages.getOrDefault(roomId, null);
    }

    public static void setMessage(Long roomId, ChatMessage message) {
        lastChatMessages.put(roomId, message);
    }
}

