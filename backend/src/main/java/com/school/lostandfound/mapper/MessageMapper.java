package com.school.lostandfound.mapper;

import com.school.lostandfound.entity.Message;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MessageMapper {
    int insert(Message message);
    List<Message> selectByUserId(@Param("userId") Integer userId); // My messages (sent or received)
    List<Message> selectChat(@Param("user1") Integer user1, @Param("user2") Integer user2);
}
