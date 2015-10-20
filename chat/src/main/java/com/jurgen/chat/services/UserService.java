package com.jurgen.chat.services;

import com.jurgen.chat.daos.ChatDAO;
import com.jurgen.chat.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userService")
public class UserService {

    @Autowired
    private ChatDAO dao;

    public UserService() {
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
