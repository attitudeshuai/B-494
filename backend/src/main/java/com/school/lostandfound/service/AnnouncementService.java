package com.school.lostandfound.service;

import com.school.lostandfound.entity.Announcement;
import java.util.List;

public interface AnnouncementService {
    void add(Announcement announcement);
    List<Announcement> listAll();
    void update(Announcement announcement);
    void delete(Integer id);
}
