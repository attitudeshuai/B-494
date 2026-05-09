package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.Admin;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    Admin selectByAccount(@Param("account") String account);
    Admin selectById(Integer id);
}
