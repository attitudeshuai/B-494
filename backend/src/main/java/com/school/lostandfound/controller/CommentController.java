package com.school.lostandfound.controller;

import com.school.lostandfound.entity.Comment;
import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.CommentService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public Result<String> add(@RequestBody Comment comment, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        
        comment.setUserId(user.getId());
        commentService.add(comment);
        return Result.success("留言成功");
    }

    @GetMapping("/list")
    public Result<List<Comment>> list(@RequestParam(required = false) Integer itemType, @RequestParam(required = false) Integer itemId) {
        if (itemType == null || itemId == null) {
            return Result.success(commentService.listAll());
        }
        return Result.success(commentService.listByItem(itemType, itemId));
    }
}
