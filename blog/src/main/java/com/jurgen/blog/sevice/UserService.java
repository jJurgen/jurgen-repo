package com.jurgen.blog.sevice;

import com.jurgen.blog.dao.RoleDao;
import com.jurgen.blog.dao.UserDao;
import com.jurgen.blog.domain.User;
import com.jurgen.blog.domain.UserRole;
import com.jurgen.blog.formbeans.SignUpFormBean;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "userService")
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService() {
        LOG.info("userService created");
    }

    public void signUp(SignUpFormBean userInfo) {
        User user = new User();
        UserRole role = roleDao.getRoleByName("ROLE_USER");
        user.setEmail(userInfo.getEmail());
        user.setUsername(userInfo.getUsername());
        user.setPassword(userInfo.getPassword());
        user.setRegDate(new Date(System.currentTimeMillis()));
        if (role != null) {
            user.getRoles().add(role);
            userDao.create(user);
        }
    }

    public boolean isUsernameExists(String username) {
        return userDao.isUsernameExists(username);
    }

    public boolean isEmailExists(String email) {
        return userDao.isEmailExists(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = (User) userDao.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username: " + username + " not found");
        }
        return user;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public RoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

}
