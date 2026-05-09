package com.school.lostandfound.service.impl;

import com.school.lostandfound.entity.Message;
import com.school.lostandfound.mapper.MessageMapper;
import com.school.lostandfound.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void send(Message message) {
        message.setSendTime(System.currentTimeMillis());
        messageMapper.insert(message);
    }

    @Override
    public List<Message> getMyMessages(Integer userId) {
        return messageMapper.selectByUserId(userId);
    }

    @Override
    public List<Message> getChat(Integer user1, Integer user2) {
        return messageMapper.selectChat(user1, user2);
    }
}
