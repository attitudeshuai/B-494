package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.User;
import com.school.lostandfound.mapper.UserMapper;
import com.school.lostandfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String account, String password) {
        User user = userMapper.selectByStudentId(account);
        if (user == null) user = userMapper.selectByEmail(account);
        if (user == null) user = userMapper.selectByPhone(account);

        if (user != null && user.getPassword().equals(password)) {
            if (user.getStatus() != null && user.getStatus() == 0) {
                throw new RuntimeException("该账号已被管理员禁用，有问题请联系管理员");
            }
            return user;
        }
        return null;
    }

    @Override
    public void register(User user) {
        // Check duplicates
        if (userMapper.selectByStudentId(user.getStudentId()) != null) {
            throw new RuntimeException("学号已存在");
        }
        user.setStatus(1); // Default active
        userMapper.insert(user);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public java.util.List<User> listAll() {
        return userMapper.selectAll();
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        userMapper.update(user);
    }

    @Override
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void resetPassword(String account, String name, String phone, String newPassword) {
        User user = userMapper.selectByStudentId(account);
        if (user == null) user = userMapper.selectByEmail(account);
        if (user == null) user = userMapper.selectByPhone(account);

        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        
        if (!user.getName().equals(name)) {
            throw new RuntimeException("姓名验证失败");
        }
        
        if (!user.getPhone().equals(phone)) {
            throw new RuntimeException("手机号验证失败");
        }

        user.setPassword(newPassword);
        userMapper.update(user);
    }
}
