package com.school.lostandfound.entity;

public class FoundItem {
    private Integer id;
    private String name;
    private String place;
    private Long foundTime;
    private String description;
    private String imagePath;
    private Integer userId;
    private Integer status;
    private Long createTime;

    private String userName; // For display

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPlace() { return place; }
    public void setPlace(String place) { this.place = place; }
    public Long getFoundTime() { return foundTime; }
    public void setFoundTime(Long foundTime) { this.foundTime = foundTime; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
