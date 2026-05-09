package com.school.lostandfound.controller;

import com.school.lostandfound.entity.Announcement;
import com.school.lostandfound.service.AnnouncementService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping("/list")
    public Result<List<Announcement>> list() {
        return Result.success(announcementService.listAll());
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody Announcement announcement, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        announcementService.add(announcement);
        return Result.success("发布成功");
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody Announcement announcement, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        announcementService.update(announcement);
        return Result.success("修改成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> delete(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        announcementService.delete(id);
        return Result.success("删除成功");
    }
}
