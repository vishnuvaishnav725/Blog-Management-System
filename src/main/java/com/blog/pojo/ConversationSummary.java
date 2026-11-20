package com.blog.pojo;

public class ConversationSummary {
    private final UserInfo user;
    private final ChatMessage latestMessage;
    private final long unreadCount;

    public ConversationSummary(UserInfo user, ChatMessage latestMessage, long unreadCount) {
        this.user = user;
        this.latestMessage = latestMessage;
        this.unreadCount = unreadCount;
    }
    public UserInfo getUser() { return user; }
    public ChatMessage getLatestMessage() { return latestMessage; }
    public long getUnreadCount() { return unreadCount; }
}
