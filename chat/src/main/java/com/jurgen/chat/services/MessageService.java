package com.jurgen.chat.services;

import com.jurgen.chat.daos.ChatDAO;
import com.jurgen.chat.entities.Message;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private ChatDAO dao;

    public MessageService() {
        LOG.info("MessageService created");
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
