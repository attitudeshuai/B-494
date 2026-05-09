package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.FoundItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface FoundItemMapper {
    int insert(FoundItem item);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int update(FoundItem item);
    List<FoundItem> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    FoundItem selectById(Integer id);
    List<FoundItem> selectByUserId(Integer userId);
    int deleteById(Integer id);
    int countAll();

    List<FoundItem> selectSimilar(@Param("name") String name, @Param("place") String place);

    List<java.util.Map<String, Object>> selectDailyStats();
}
