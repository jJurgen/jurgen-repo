package com.jurgen.blog.dao;

import com.jurgen.blog.domain.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

@Component("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Integer> implements UserDao {

    @Override
    public boolean isUsernameExists(String username) {
        User user = (User) getCurrentSession().createCriteria(type).add(Restrictions.eq("username", username)).uniqueResult();
        return user != null;
    }

    @Override
    public boolean isEmailExists(String email) {
        User user = (User) getCurrentSession().createCriteria(type).add(Restrictions.eq("email", email)).uniqueResult();
        return user != null;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = (User) getCurrentSession().createCriteria(type).add(Restrictions.eq("username", username)).uniqueResult();
        return user;
    }

}
