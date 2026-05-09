package com.school.lostandfound.controller;

import com.school.lostandfound.entity.User;
import com.school.lostandfound.service.UserService;
import com.school.lostandfound.utils.Result;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<User> login(@RequestBody User loginUser, HttpSession session) {
        try {
            User user = userService.login(loginUser.getStudentId(), loginUser.getPassword()); // reuse studentId field for account
            if (user != null) {
                session.setAttribute("user", user);
                return Result.success(user);
            }
            return Result.error("用户名或密码错误");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        try {
            userService.register(user);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<User> info(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return Result.error("未登录");
        // refresh from db
        return Result.success(userService.getById(user.getId()));
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return Result.error("未登录");
        
        user.setId(currentUser.getId()); // ensure update own
        userService.update(user);
        session.setAttribute("user", userService.getById(currentUser.getId()));
        return Result.success("更新成功");
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }

    @PostMapping("/reset-password")
    public Result<String> resetPassword(@RequestBody java.util.Map<String, String> params) {
        try {
            String account = params.get("account");
            String name = params.get("name");
            String phone = params.get("phone");
            String newPassword = params.get("newPassword");
            
            if (account == null || name == null || phone == null || newPassword == null) {
                return Result.error("参数不完整");
            }

            userService.resetPassword(account, name, phone, newPassword);
            return Result.success("密码重置成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/list")
    public Result<java.util.List<User>> list() {
        return Result.success(userService.listAll());
    }
}
