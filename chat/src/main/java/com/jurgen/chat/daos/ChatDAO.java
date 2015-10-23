package com.jurgen.chat.daos;

import com.jurgen.chat.entities.Message;
import com.jurgen.chat.entities.User;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class ChatDAO {
    
    private static final Logger LOG = LoggerFactory.getLogger(ChatDAO.class);
    private final static String ADD_USER = "INSERT INTO chatdb.users(nickname, password)  VALUES(?,?)";
    private final static String GET_USER_COUNT = "SELECT COUNT(*) FROM chatdb.users WHERE users.nickname = ? AND users.password = ?";
    private final static String GET_USER = "SELECT * FROM chatdb.users WHERE users.nickname = ? AND users.password = ?";
    private final static String ADD_MESSAGE = "INSERT INTO chatdb.messages(content,time,user_id) VALUES(?,?,(SELECT id FROM chatdb.users WHERE nickname = ?))";
    private final static String GET_ALL_MESSAGES = "SELECT messages.content, messages.time, users.nickname FROM chatdb.messages JOIN chatdb.users ON messages.user_id = users.id";
    
    private final Driver mysql;
    
    @Autowired
    private JdbcTemplate template;
    
    public ChatDAO() throws SQLException {
        mysql = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(mysql);
        LOG.warn("dao created!");
    }
    
    public void addUser(String nickname, String password) {
        try {
            template.update(ADD_USER, nickname, password);
            LOG.debug("User: " + nickname + " added");
        } catch (DataAccessException ex) {
            LOG.error("User(" + nickname + "," + password + ") wasn't added with error: " + ex.getMessage());
        }
    }
    
    public Boolean canUserSignIn(String nickname, String password) {
        try {
            return template.query(GET_USER_COUNT, new Object[]{nickname, password}, new ResultSetExtractor<Boolean>() {
                @Override
                public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        if (rs.getInt(1) == 1) {
                            return true;
                        }
                    }
                    return false;
                }
            });
        } catch (DataAccessException ex) {
            LOG.error("Can't check user info(nickname=" + nickname + ", password=" + password + ") with error: " + ex.getMessage());
            return false;
        }
    }
    
    public User getUser(String nickname, String password) {
        try {
            return template.query(GET_USER, new Object[]{nickname, password}, new ResultSetExtractor<User>() {
                @Override
                public User extractData(ResultSet rs) throws SQLException, DataAccessException {
                    if (rs.next()) {
                        String nick = rs.getString("nickname");
                        String pass = rs.getString("password");
                        User user = new User(nick, pass);
                        LOG.debug("User: " + nickname + " was taken from db");
                        return user;
                    }
                    return null;
                }
            });
        } catch (DataAccessException ex) {
            LOG.error("Can't get user(nickname=" + nickname + ", password=" + password + ")from database with error: " + ex.getMessage());
            return null;
        }
    }
    
    public void addMessage(String content, String author) {
        try {
            Time time = new Time(new java.util.Date().getTime());
            template.update(ADD_MESSAGE, content, time, author);
            LOG.debug("Message from " + author + " added to database");
        } catch (DataAccessException ex) {
            LOG.error("Message from " + author + " wasn't added to database with error: " + ex.getMessage());
        }
    }
    
    public List<Message> getMessages() {
        try {
            return template.query(GET_ALL_MESSAGES, new RowMapper<Message>() {
                @Override
                public Message mapRow(ResultSet rs, int i) throws SQLException {
                    String content = rs.getString("content");
                    Time time = rs.getTime("time");
                    String author = rs.getString("nickname");
                    return new Message(content, author, time);
                }
            });
        } catch (DataAccessException ex) {
            LOG.error("Can't get all messages from database with error: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
    
    public JdbcTemplate getTemplate() {
        return template;
    }
    
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }
    
}
