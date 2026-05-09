package com.school.lostandfound.controller;

import com.school.lostandfound.entity.LostItem;
import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.FoundItemService;
import com.school.lostandfound.service.LostItemService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lost")
public class LostItemController {

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @PostMapping("/publish")
    public Result<String> publish(@RequestBody LostItem item, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        
        item.setUserId(user.getId());
        lostItemService.publish(item);
        return Result.success("发布成功，等待审核");
    }

    @GetMapping("/list")
    public Result<List<LostItem>> list(@RequestParam(required = false) String keyword) {
        return Result.success(lostItemService.list(keyword, 1)); // Only show approved (status=1)
    }

    @GetMapping("/detail")
    public Result<LostItem> detail(@RequestParam Integer id) {
        return Result.success(lostItemService.getById(id));
    }

    @GetMapping("/my")
    public Result<List<LostItem>> my(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        return Result.success(lostItemService.getMyItems(user.getId()));
    }

    @GetMapping("/match")
    public Result<List<com.school.lostandfound.entity.FoundItem>> match(@RequestParam Integer id) {
        LostItem item = lostItemService.getById(id);
        if (item == null) return Result.error("物品不存在");
        return Result.success(foundItemService.findSimilar(item.getName(), item.getPlace()));
    }
}
