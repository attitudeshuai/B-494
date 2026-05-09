package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.Comment;
import com.school.lostandfound.mapper.CommentMapper;
import com.school.lostandfound.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void add(Comment comment) {
        comment.setCreateTime(System.currentTimeMillis());
        comment.setStatus(0); // Default pending audit
        commentMapper.insert(comment);
    }

    @Override
    public List<Comment> listByItem(Integer itemType, Integer itemId) {
        return commentMapper.selectByItemId(itemType, itemId);
    }

    @Override
    public List<Comment> listAll() {
        return commentMapper.selectAll();
    }

    @Override
    public void delete(Integer id) {
        commentMapper.deleteById(id);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.update(comment);
    }
}
