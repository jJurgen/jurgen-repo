package com.jurgen.chat.services;

import com.jurgen.chat.domain.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService implements UserDetailsService {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        LOG.info("userService created");
    }

    public void addUser(User user) {
        final Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from User where nickname = :nickname");
            query.setParameter("nickname", nickname);
            User user = (User) query.uniqueResult();
            if (user == null) {
                throw new UsernameNotFoundException("nickname: " + nickname + "not found");
            }
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
