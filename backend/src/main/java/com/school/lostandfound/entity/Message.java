package com.school.lostandfound.entity;

public class Message {
    private Integer id;
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Long sendTime;

    private String senderName;
    private String senderAvatar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSenderId() { return senderId; }
    public void setSenderId(Integer senderId) { this.senderId = senderId; }
    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getSendTime() { return sendTime; }
    public void setSendTime(Long sendTime) { this.sendTime = sendTime; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderAvatar() { return senderAvatar; }
    public void setSenderAvatar(String senderAvatar) { this.senderAvatar = senderAvatar; }
}
