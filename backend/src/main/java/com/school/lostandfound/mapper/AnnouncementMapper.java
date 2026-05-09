package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AnnouncementMapper {
    void insert(Announcement announcement);
    List<Announcement> selectAll();
    Announcement selectById(Integer id);
    void deleteById(Integer id);
    void update(Announcement announcement);
}
