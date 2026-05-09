package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.Announcement;
import com.school.lostandfound.mapper.AnnouncementMapper;
import com.school.lostandfound.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public void add(Announcement announcement) {
        announcement.setCreateTime(System.currentTimeMillis());
        announcement.setUpdateTime(System.currentTimeMillis());
        announcementMapper.insert(announcement);
    }

    @Override
    public List<Announcement> listAll() {
        return announcementMapper.selectAll();
    }

    @Override
    public void update(Announcement announcement) {
        announcement.setUpdateTime(System.currentTimeMillis());
        announcementMapper.update(announcement);
    }

    @Override
    public void delete(Integer id) {
        announcementMapper.deleteById(id);
    }
}
