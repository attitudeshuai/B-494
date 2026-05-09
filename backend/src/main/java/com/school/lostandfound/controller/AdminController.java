package com.school.lostandfound.controller;

import com.school.lostandfound.entity.Admin;
import com.school.lostandfound.entity.FoundItem;
import com.school.lostandfound.entity.LostItem;
import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.AdminService;
import com.school.lostandfound.service.CommentService;
import com.school.lostandfound.service.FoundItemService;
import com.school.lostandfound.service.LostItemService;
import com.school.lostandfound.service.UserService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private LostItemService lostItemService;
    @Autowired
    private FoundItemService foundItemService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/login")
    public Result<Admin> login(@RequestBody Admin loginAdmin, HttpSession session) {
        Admin admin = adminService.login(loginAdmin.getAccount(), loginAdmin.getPassword());
        if (admin != null) {
            session.setAttribute("admin", admin);
            return Result.success(admin);
        }
        return Result.error("账号或密码错误");
    }

    @GetMapping("/info")
    public Result<Admin> info(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) return Result.error("未登录");
        return Result.success(admin);
    }

    @GetMapping("/lost/list")
    public Result<List<LostItem>> listLost(@RequestParam(required = false) Integer status, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        return Result.success(lostItemService.list(null, status));
    }

    @PostMapping("/lost/audit")
    public Result<String> auditLost(@RequestBody LostItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        lostItemService.updateStatus(item.getId(), item.getStatus());
        return Result.success("操作成功");
    }

    @GetMapping("/found/list")
    public Result<List<FoundItem>> listFound(@RequestParam(required = false) Integer status, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        return Result.success(foundItemService.list(null, status));
    }

    @PostMapping("/found/audit")
    public Result<String> auditFound(@RequestBody FoundItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        foundItemService.updateStatus(item.getId(), item.getStatus());
        return Result.success("操作成功");
    }

    @GetMapping("/stats")
    public Result<java.util.Map<String, Object>> stats(HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("lost", lostItemService.getDailyStats());
        map.put("found", foundItemService.getDailyStats());
        return Result.success(map);
    }
    
    @GetMapping("/user/list")
    public Result<List<User>> listUsers(HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        return Result.success(userService.listAll());
    }

    @PostMapping("/user/status")
    public Result<String> userStatus(@RequestBody User user, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        userService.updateStatus(user.getId(), user.getStatus());
        return Result.success("操作成功");
    }

    @PostMapping("/user/add")
    public Result<String> addUser(@RequestBody User user, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        userService.register(user);
        return Result.success("添加成功");
    }

    @PostMapping("/user/update")
    public Result<String> updateUser(@RequestBody User user, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        userService.update(user);
        return Result.success("修改成功");
    }

    @DeleteMapping("/user/{id}")
    public Result<String> deleteUser(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        userService.delete(id);
        return Result.success("删除成功");
    }

    @PostMapping("/lost/add")
    public Result<String> addLost(@RequestBody LostItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        Integer desiredStatus = item.getStatus();
        lostItemService.publish(item);
        if (desiredStatus != null) {
            item.setStatus(desiredStatus);
            lostItemService.update(item);
        }
        return Result.success("添加成功");
    }

    @PostMapping("/lost/update")
    public Result<String> updateLost(@RequestBody LostItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        lostItemService.update(item);
        return Result.success("修改成功");
    }

    @DeleteMapping("/lost/{id}")
    public Result<String> deleteLost(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        lostItemService.delete(id);
        return Result.success("删除成功");
    }

    @PostMapping("/found/add")
    public Result<String> addFound(@RequestBody FoundItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        Integer desiredStatus = item.getStatus();
        foundItemService.publish(item);
        if (desiredStatus != null) {
            item.setStatus(desiredStatus);
            foundItemService.update(item);
        }
        return Result.success("添加成功");
    }

    @PostMapping("/found/update")
    public Result<String> updateFound(@RequestBody FoundItem item, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        foundItemService.update(item);
        return Result.success("修改成功");
    }

    @DeleteMapping("/found/{id}")
    public Result<String> deleteFound(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        foundItemService.delete(id);
        return Result.success("删除成功");
    }

    @DeleteMapping("/comment/{id}")
    public Result<String> deleteComment(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        commentService.delete(id);
        return Result.success("删除成功");
    }

    @PostMapping("/comment/update")
    public Result<String> updateComment(@RequestBody com.school.lostandfound.entity.Comment comment, HttpSession session) {
        if (session.getAttribute("admin") == null) return Result.error("未登录");
        commentService.update(comment);
        return Result.success("修改成功");
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }
}
