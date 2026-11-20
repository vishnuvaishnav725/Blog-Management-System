package com.blog.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "chat_messages",
    indexes = {
        @Index(name = "idx_chat_sender_receiver_created", columnList = "sender_id,receiver_id,created_at"),
        @Index(name = "idx_chat_receiver_seen", columnList = "receiver_id,is_seen,created_at")
    },
    uniqueConstraints = @UniqueConstraint(name = "uk_chat_sender_client_token", columnNames = {"sender_id", "client_token"}))
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserInfo sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserInfo receiver;

    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "image_path", length = 80)
    private String imagePath;

    @Column(name = "message_type", nullable = false, length = 16)
    private String messageType;

    @Column(name = "is_seen", nullable = false)
    private boolean seen;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "client_token", length = 36)
    private String clientToken;

    @PrePersist
    protected void beforeInsert() {
        if (createdAt == null) createdAt = new Date();
        if (messageType == null) messageType = imagePath == null ? "TEXT" : "IMAGE";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public UserInfo getSender() { return sender; }
    public void setSender(UserInfo sender) { this.sender = sender; }
    public UserInfo getReceiver() { return receiver; }
    public void setReceiver(UserInfo receiver) { this.receiver = receiver; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public String getClientToken() { return clientToken; }
    public void setClientToken(String clientToken) { this.clientToken = clientToken; }
}
