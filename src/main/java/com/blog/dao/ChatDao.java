package com.blog.dao;

import java.util.List;
import com.blog.pojo.ChatMessage;

public interface ChatDao {
    ChatMessage findByClientToken(int senderId, String token);
    ChatMessage save(ChatMessage message);
    List<ChatMessage> getConversation(int firstUserId, int secondUserId);
    List<ChatMessage> getRecentMessages(int userId);
    long getUnreadCount(int receiverId, int senderId);
    int markConversationSeen(int receiverId, int senderId);
    ChatMessage getMessage(int id);
}
