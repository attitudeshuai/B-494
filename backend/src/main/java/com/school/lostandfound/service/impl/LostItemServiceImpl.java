package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.LostItem;
import com.school.lostandfound.mapper.LostItemMapper;
import com.school.lostandfound.service.LostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LostItemServiceImpl implements LostItemService {

    @Autowired
    private LostItemMapper lostItemMapper;

    @Override
    public void publish(LostItem item) {
        item.setStatus(0);
        item.setCreateTime(System.currentTimeMillis());
        lostItemMapper.insert(item);
    }

    @Override
    public List<LostItem> list(String keyword, Integer status) {
        return lostItemMapper.selectList(keyword, status);
    }

    @Override
    public LostItem getById(Integer id) {
        return lostItemMapper.selectById(id);
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        lostItemMapper.updateStatus(id, status);
    }

    @Override
    public void update(LostItem item) {
        lostItemMapper.update(item);
    }

    @Override
    public void delete(Integer id) {
        lostItemMapper.deleteById(id);
    }

    @Override
    public List<LostItem> getMyItems(Integer userId) {
        return lostItemMapper.selectByUserId(userId);
    }

    @Override
    public List<LostItem> findSimilar(String name, String place) {
        return lostItemMapper.selectSimilar(name, place);
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getDailyStats() {
        return lostItemMapper.selectDailyStats();
    }
}
