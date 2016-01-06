package com.jurgen.chat.services;

import com.jurgen.chat.domain.Message;
import com.jurgen.chat.domain.User;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "messageService")
public class MessageService {

    @Autowired
    private SessionFactory sessionFactory;
    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    public MessageService() {
        LOG.info("messageService created");
    }

    public void addMessage(Message message) {
        Session session = sessionFactory.openSession();
        try {
            session.save(message);
        } finally {
            session.close();
        }
    }

    public List<Message> getMessages() {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from Message");
            List messages = query.list();
            return messages;
        } finally {
            session.close();
        }
    }
}
