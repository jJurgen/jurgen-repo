package com.jurgen.chat.services;

import com.jurgen.chat.domain.UserRole;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleService {

    @Autowired
    private SessionFactory sessionFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public RoleService() {
        LOG.info("roleService created");
    }

    public UserRole getRoleByName(String roleName) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("from UserRole where role = :role");
            query.setParameter("role", roleName);
            UserRole role = (UserRole) query.uniqueResult();
            return role;
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
