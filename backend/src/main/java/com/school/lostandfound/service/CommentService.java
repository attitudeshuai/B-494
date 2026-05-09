package com.school.lostandfound.service;

import com.school.lostandfound.entity.Comment;
import java.util.List;

public interface CommentService {
    void add(Comment comment);
    List<Comment> listByItem(Integer itemType, Integer itemId);
    List<Comment> listAll();
    void delete(Integer id);
    void update(Comment comment);
}
