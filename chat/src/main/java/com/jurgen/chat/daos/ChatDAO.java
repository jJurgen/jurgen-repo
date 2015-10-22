package com.jurgen.chat.daos;

import com.jurgen.chat.entities.Message;
import com.jurgen.chat.entities.User;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatDAO {
    private static final Logger LOG = LoggerFactory.getLogger(ChatDAO.class);
    
    private final static String ADD_USER = "INSERT INTO chatdb.users(nickname, password)  VALUES(?,?)";
    private final static String GET_USER_COUNT = "SELECT COUNT(*) FROM chatdb.users WHERE users.nickname = ? AND users.password = ?";
    private final static String GET_USER = "SELECT * FROM chatdb.users WHERE users.nickname = ? AND users.password = ?";
    private final static String ADD_MESSAGE = "INSERT INTO chatdb.messages(content,time,user_id) VALUES(?,?,(SELECT id FROM chatdb.users WHERE nickname = ?))";
    private final static String GET_ALL_MESSAGES = "SELECT messages.content, messages.time, users.nickname FROM chatdb.messages JOIN chatdb.users ON messages.user_id = users.id";

    private final Driver mysql;
    private Connection conn;
    private static final String CONNECTION_URL = "jdbc:mysql://localhost/chatdb";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public ChatDAO() throws SQLException {
        mysql = new com.mysql.jdbc.Driver();
        DriverManager.registerDriver(mysql);
        conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
        LOG.warn("dao created!");
    }

    public boolean addUser(String nickname, String password) {
        try (PreparedStatement ps = conn.prepareStatement(ADD_USER)) {
            ps.setString(1, nickname);
            ps.setString(2, password);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        LOG.info("User: " + nickname + " added");
        return true;
    }

    public boolean canUserSignIn(String nickname, String password) {
        try (PreparedStatement ps = conn.prepareStatement(GET_USER_COUNT)) {
            ps.setString(1, nickname);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public User getUser(String nickname, String password) {
        try (PreparedStatement ps = conn.prepareStatement(GET_USER)) {
            ps.setString(1, nickname);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String nick = rs.getString("nickname");
                String pass = rs.getString("password");
                User user = new User(nick, pass);
                LOG.info("User: " + nickname + " was taken from db");
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean addMessage(String content, String author) {
        try (PreparedStatement ps = conn.prepareStatement(ADD_MESSAGE)) {
            ps.setString(1, content);
            Time sqlTime = new Time(new Date().getTime());
            ps.setTime(2, sqlTime);
            ps.setString(3, author);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        LOG.info(author + " sended message:" + content.substring(0,Math.min(5, content.length())));
        return true;
    }

    public List<Message> getMessages() {
        try (PreparedStatement ps = conn.prepareStatement(GET_ALL_MESSAGES)) {
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                String content = rs.getString("content");
                Time time = rs.getTime("time");
                String author = rs.getString("nickname");
                messages.add(new Message(content, author, time));
            }
            LOG.debug("All messages was taken from db");
            return messages;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
