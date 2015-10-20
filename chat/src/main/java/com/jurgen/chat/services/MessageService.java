package com.jurgen.chat.services;

import com.jurgen.chat.daos.ChatDAO;
import com.jurgen.chat.entities.Message;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageService {

    @Autowired
    private ChatDAO dao;

    public MessageService() {

    }

    public void addMessage(Message message) {
        dao.addMessage(message.getMessage(), message.getAuthor());
    }

    public List<Message> getMessages() {
        return dao.getMessages();
    }

    public ChatDAO getDao() {
        return dao;
    }

    public void setDao(ChatDAO dao) {
        this.dao = dao;
    }

}
