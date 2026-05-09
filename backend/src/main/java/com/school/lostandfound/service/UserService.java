package com.school.lostandfound.service;

import com.school.lostandfound.entity.User;

public interface UserService {
    User login(String account, String password); // Account can be email/phone/studentId
    void register(User user);
    void update(User user);
    User getById(Integer id);
    java.util.List<User> listAll();
    void updateStatus(Integer id, Integer status);
    void delete(Integer id);
    void resetPassword(String account, String name, String phone, String newPassword);
}
