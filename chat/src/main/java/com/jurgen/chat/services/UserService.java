package com.jurgen.chat.services;

import com.jurgen.chat.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        LOG.info("UserService created");
    }

    public void addUser(String nickname, String password) {
        final Session session = sessionFactory.openSession();
        try {
            User user = new User(nickname, password);
            session.save(user);
        } finally {
            session.close();
        }
    }

    public User signIn(String nickname, String password) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from User where nickname = :nickname and password = :password");
            query.setParameter("nickname", nickname);
            query.setParameter("password", password);
            User user = (User) query.uniqueResult();
            return user;
        } finally {
            session.close();
        }      
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
