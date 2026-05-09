package com.school.lostandfound.service;

import com.school.lostandfound.entity.FoundItem;
import java.util.List;

public interface FoundItemService {
    void publish(FoundItem item);
    List<FoundItem> list(String keyword, Integer status);
    FoundItem getById(Integer id);
    void updateStatus(Integer id, Integer status);
    void update(FoundItem item);
    void delete(Integer id);
    List<FoundItem> getMyItems(Integer userId);
    List<FoundItem> findSimilar(String name, String place);
    java.util.List<java.util.Map<String, Object>> getDailyStats();
}
