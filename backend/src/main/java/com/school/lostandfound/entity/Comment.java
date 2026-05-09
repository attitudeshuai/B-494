package com.school.lostandfound.entity;

public class Comment {
    private Integer id;
    private Integer userId;
    private Integer itemType;
    private Integer itemId;
    private String content;
    private Integer parentId; // Reply to comment ID
    private Integer status; // 0: Pending, 1: Approved
    private Long createTime;

    private String userName;
    private String userAvatar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getItemType() { return itemType; }
    public void setItemType(Integer itemType) { this.itemType = itemType; }
    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserAvatar() { return userAvatar; }
    public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }
}
