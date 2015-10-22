package com.jurgen.chat.services;

import com.jurgen.chat.daos.ChatDAO;
import com.jurgen.chat.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService {
    
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private ChatDAO dao;
    
    public UserService() {
        LOG.info("UserService created");
    }
    
    public boolean addUser(String nickname, String password) {
        return dao.addUser(nickname, password);
    }
    
    public User signIn(String nickname, String password) {
        if (dao.canUserSignIn(nickname, password)) {
            return dao.getUser(nickname, password);
        }
        return null;
    }
    
    public ChatDAO getDao() {
        return dao;
    }
    
    public void setDao(ChatDAO dao) {
        this.dao = dao;
    }
    
}
