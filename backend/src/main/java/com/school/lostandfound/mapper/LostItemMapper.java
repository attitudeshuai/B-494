package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.LostItem;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface LostItemMapper {
    int insert(LostItem item);
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    int update(LostItem item);
    List<LostItem> selectList(@Param("keyword") String keyword, @Param("status") Integer status);
    LostItem selectById(Integer id);
    List<LostItem> selectByUserId(Integer userId);
    int deleteById(Integer id);
    // For statistics
    int countAll();
    
    // Find similar items (FoundItems matching LostItem) - actually this logic belongs to Service calling FoundItemMapper
    // But we need a method in FoundItemMapper to search by multiple fields

    List<LostItem> selectSimilar(@Param("name") String name, @Param("place") String place);

    List<java.util.Map<String, Object>> selectDailyStats();
}
