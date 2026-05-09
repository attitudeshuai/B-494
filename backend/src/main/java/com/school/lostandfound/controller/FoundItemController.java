package com.school.lostandfound.controller;

import com.school.lostandfound.entity.FoundItem;
import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.FoundItemService;
import com.school.lostandfound.service.LostItemService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/found")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private LostItemService lostItemService;

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody FoundItem item, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        
        item.setUserId(user.getId());
        foundItemService.publish(item);
        return Result.success("发布成功，等待审核");
    }

    @GetMapping("/list")
    public Result<List<FoundItem>> list(@RequestParam(required = false) String keyword) {
        return Result.success(foundItemService.list(keyword, 1)); // Only show approved
    }

    @GetMapping("/detail")
    public Result<FoundItem> detail(@RequestParam Integer id) {
        return Result.success(foundItemService.getById(id));
    }

    @GetMapping("/my")
    public Result<List<FoundItem>> my(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        return Result.success(foundItemService.getMyItems(user.getId()));
    }

    @GetMapping("/match")
    public Result<List<com.school.lostandfound.entity.LostItem>> match(@RequestParam Integer id) {
        FoundItem item = foundItemService.getById(id);
        if (item == null) return Result.error("物品不存在");
        return Result.success(lostItemService.findSimilar(item.getName(), item.getPlace()));
    }
}
