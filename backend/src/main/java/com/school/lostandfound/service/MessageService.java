package com.school.lostandfound.service;

import com.school.lostandfound.entity.Message;
import java.util.List;

public interface MessageService {
    void send(Message message);
    List<Message> getMyMessages(Integer userId);
    List<Message> getChat(Integer user1, Integer user2);
}
