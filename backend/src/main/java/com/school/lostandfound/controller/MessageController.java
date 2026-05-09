package com.school.lostandfound.controller;

import com.school.lostandfound.entity.Message;
import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.MessageService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/send")
    public Result<String> send(@RequestBody Message message, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        
        message.setSenderId(user.getId());
        messageService.send(message);
        return Result.success("发送成功");
    }

    @GetMapping("/my")
    public Result<List<Message>> my(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        return Result.success(messageService.getMyMessages(user.getId()));
    }
    
    @GetMapping("/chat")
    public Result<List<Message>> chat(@RequestParam Integer otherUserId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        return Result.success(messageService.getChat(user.getId(), otherUserId));
    }
}
