package com.school.lostandfound.service;

import com.school.lostandfound.entity.LostItem;
import java.util.List;

public interface LostItemService {
    void publish(LostItem item);
    List<LostItem> list(String keyword, Integer status); // status null means all (for admin?)
    LostItem getById(Integer id);
    void updateStatus(Integer id, Integer status);
    void update(LostItem item);
    void delete(Integer id);
    List<LostItem> getMyItems(Integer userId);
    List<LostItem> findSimilar(String name, String place);
    java.util.List<java.util.Map<String, Object>> getDailyStats();
}
