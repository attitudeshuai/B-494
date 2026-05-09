package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {
    int insert(User user);
    User selectById(Integer id);
    User selectByStudentId(String studentId);
    User selectByEmail(String email);
    User selectByPhone(String phone);
    int update(User user);
    List<User> selectAll();
    int deleteById(Integer id);
}
