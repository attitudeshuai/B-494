package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.Comment;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CommentMapper {
    int insert(Comment comment);
    List<Comment> selectByItemId(@Param("itemType") Integer itemType, @Param("itemId") Integer itemId);
    List<Comment> selectAll();

    void deleteById(Integer id);

    void update(Comment comment);
}
