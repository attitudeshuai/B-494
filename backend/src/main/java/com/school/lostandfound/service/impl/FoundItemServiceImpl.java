package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.FoundItem;
import com.school.lostandfound.mapper.FoundItemMapper;
import com.school.lostandfound.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoundItemServiceImpl implements FoundItemService {

    @Autowired
    private FoundItemMapper foundItemMapper;

    @Override
    public void publish(FoundItem item) {
        item.setStatus(0); // Pending audit
        item.setCreateTime(System.currentTimeMillis());
        foundItemMapper.insert(item);
    }

    @Override
    public List<FoundItem> list(String keyword, Integer status) {
        return foundItemMapper.selectList(keyword, status);
    }

    @Override
    public FoundItem getById(Integer id) {
        return foundItemMapper.selectById(id);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        foundItemMapper.updateStatus(id, status);
    }

    @Override
    public void update(FoundItem item) {
        foundItemMapper.update(item);
    }

    @Override
    public void delete(Integer id) {
        foundItemMapper.deleteById(id);
    }

    @Override
    public List<FoundItem> getMyItems(Integer userId) {
        return foundItemMapper.selectByUserId(userId);
    }

    @Override
    public List<FoundItem> findSimilar(String name, String place) {
        return foundItemMapper.selectSimilar(name, place);
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getDailyStats() {
        return foundItemMapper.selectDailyStats();
    }
}
