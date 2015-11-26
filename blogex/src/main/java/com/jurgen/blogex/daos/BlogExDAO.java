package com.jurgen.blogex.daos;

import com.jurgen.blogex.entities.Comment;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class BlogExDAO {

	private static final String ADD_NEW_USER = "INSERT INTO blogexdb.users(email, nickname, password, regDate) VALUES(?,?,?,curdate())";
	private static final String CHECK_EMAIL_UNIQUE = "SELECT COUNT(email) FROM blogexdb.users WHERE email = ?";
	private static final String CHECK_NICKNAME_UNIQUE = "SELECT COUNT(nickname) FROM blogexdb.users WHERE nickname = ?";
	private static final String CAN_USER_SIGN_IN = "SELECT COUNT(*) FROM blogexdb.users WHERE users.email = ? and users.password = ?";
	private static final String GET_NICKNAME_BY_EMAIL = "SELECT users.nickname FROM blogexdb.users WHERE email = ?";
	private static final String SIGN_IN = "SELECT email, nickname, isAdmin, regDate, about FROM blogexdb.users WHERE email = ? and password = ?";
	private static final String GET_POSTS = "SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id AND users.id = (SELECT id FROM blogexdb.users WHERE email = ?)";
	private static final String GET_POST = "SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.id = ? AND posts.userId = users.id";
	private static final String ADD_NEW_POST = "INSERT INTO blogexdb.posts(title,content,userId,postDate) VALUES(?,?,(SELECT id FROM blogexdb.users WHERE email = ?),curdate())";
	private static final String GET_POST_COMMENTS = "SELECT * FROM (SELECT comments.id, comments.content,comments.commentTime,users.nickname FROM blogexdb.comments INNER JOIN blogexdb.users ON comments.postId = ? AND comments.userId = users.id) as tmp ORDER BY commentTime";
	private static final String ADD_COMMENT = "INSERT INTO blogexdb.comments(postId,content,commentTime,userId) VALUES(?,?,current_timestamp(),(SELECT id FROM blogexdb.users WHERE email = ?))";
	private static final String GET_LAST_POSTS = "SELECT * FROM (SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id ORDER BY id DESC LIMIT ?)as tmp ORDER BY id DESC";
	private static final String GET_POSTS_BY_AUTHOR = "SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id AND locate(?,users.nickname)";
	private static final String GET_POSTS_BY_TITLE = "SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id AND locate(?,posts.title)";
	private static final String GET_POSTS_BY_CONTENT = "SELECT posts.id, posts.title,posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id AND locate(?,posts.content)";
	private static final String GET_POSTS_BY_COMMENT = "SELECT posts.id, posts.title, posts.content, posts.postDate, users.nickname FROM blogexdb.posts JOIN blogexdb.users ON posts.userId = users.id AND posts.id IN (SELECT postId FROM blogexdb.comments WHERE LOCATE(?,comments.content))";
	private static final String MAKE_ADMIN = "UPDATE blogexdb.users SET users.isAdmin = 1 WHERE nickname = ?";
	private static final String DELETE_COMMENT = "DELETE FROM blogexdb.comments WHERE comments.id = ?";
	private static final String DELETE_POST_COMMENTS = "DELETE FROM blogexdb.comments WHERE comments.postId = ?";
	private static final String DELETE_POST = "DELETE FROM blogexdb.posts WHERE posts.id = ?";
	private static final String GET_USER_BY_NICKNAME = "SELECT email, nickname, isAdmin, regDate, about FROM blogexdb.users WHERE users.nickname = ?";
	private static final String UPDATE_USER_INFO = "UPDATE blogexdb.users SET users.about = ? WHERE users.email = ?";
	private static final String ADD_USER_TO_BLACKLIST = "INSERT IGNORE INTO blogexdb.blacklist(userId) VALUES((SELECT id FROM blogexdb.users WHERE nickname = ?))";
	private static final String IS_USER_BLOCKED = "SELECT COUNT(*) FROM blogexdb.blacklist where userId = (SELECT id FROM blogexdb.users WHERE email = ?)";
	private static final String UPDATE_POST = "UPDATE blogexdb.posts SET posts.title = ?, posts.content = ? WHERE posts.id = ?";
	private static final String UNBLOCK_USER = "DELETE FROM blogexdb.blacklist WHERE blacklist.userId = (SELECT id FROM blogexdb.users WHERE users.nickname = ?)";
	private static final String GET_BLACKLIST = "SELECT nickname FROM blogexdb.users WHERE users.id IN (SELECT userId FROM blogexdb.blacklist)";
	private static final String ADD_NEW_TOKEN = "INSERT INTO blogexdb.tokens(token,userId) VALUES(?,(SELECT id FROM blogexdb.users WHERE users.email = ?))";
	private static final String GET_USER_BY_TOKEN = "SELECT email, nickname, isAdmin, regDate, about FROM blogexdb.users WHERE users.id = (SELECT userId FROM blogexdb.tokens WHERE token = ?)";
	private static final String REMOVE_TOKEN = "DELETE FROM blogexdb.tokens WHERE token = ?";

	private final Driver mysql = new com.mysql.jdbc.Driver();
	private static final String CONNECTION_URL = "jdbc:mysql://localhost/blogexdb";
	private static final String USER = "root";
	private static final String PASSWORD = "123456";
	private Connection conn;

	public BlogExDAO() throws SQLException {
		DriverManager.registerDriver(mysql);
		conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
	}

	public boolean addUserToBlackList(String nickname) {
		try (PreparedStatement ps = conn.prepareStatement(ADD_USER_TO_BLACKLIST)) {
			ps.setString(1, nickname);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeToken(String token) {
		try (PreparedStatement ps = conn.prepareStatement(REMOVE_TOKEN)) {
			ps.setString(1, token);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addToken(String token, String email) {
		try (PreparedStatement ps = conn.prepareStatement(ADD_NEW_TOKEN)) {
			ps.setString(1, token);
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean unblockUser(String nickname) {
		try (PreparedStatement ps = conn.prepareStatement(UNBLOCK_USER)) {
			ps.setString(1, nickname);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<String> getBlackList() {
		try (PreparedStatement ps = conn.prepareStatement(GET_BLACKLIST)) {
			ResultSet rs = ps.executeQuery();
			List<String> blacklist = new ArrayList<>();
			while (rs.next()) {
				blacklist.add(rs.getString("nickname"));
			}
			return blacklist;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean addUser(String email, String nickname, String password) {
		try (PreparedStatement ps = conn.prepareStatement(ADD_NEW_USER)) {
			ps.setString(1, email);
			ps.setString(2, nickname);
			ps.setString(3, password);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateUserInfo(String email, String about) {
		try (PreparedStatement ps = conn.prepareStatement(UPDATE_USER_INFO)) {
			ps.setString(1, about);
			ps.setString(2, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updatePost(String title, String content, int id) {
		try (PreparedStatement ps = conn.prepareStatement(UPDATE_POST)) {
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setInt(3, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public User getUserByNickname(String nickname) {
		try (PreparedStatement ps = conn.prepareStatement(GET_USER_BY_NICKNAME)) {
			ps.setString(1, nickname);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String email = rs.getString("email");
				String nick = rs.getString("nickname");
				boolean isAdmin = rs.getBoolean("isAdmin");
				Date regDate = rs.getDate("regDate");
				String about = rs.getString("about");
				User user = new User(nick, email, regDate, isAdmin, about);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserByToken(String token) {
		try (PreparedStatement ps = conn.prepareStatement(GET_USER_BY_TOKEN)) {
			ps.setString(1, token);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String email = rs.getString("email");
				String nick = rs.getString("nickname");
				boolean isAdmin = rs.getBoolean("isAdmin");
				Date regDate = rs.getDate("regDate");
				String about = rs.getString("about");
				User user = new User(nick, email, regDate, isAdmin, about);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean removePost(int postId) {
		if (!removeComments(postId)) {
			return false;
		}
		try (PreparedStatement ps = conn.prepareStatement(DELETE_POST)) {
			ps.setInt(1, postId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeComments(int postId) {
		try (PreparedStatement ps = conn.prepareStatement(DELETE_POST_COMMENTS)) {
			ps.setInt(1, postId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean removeComment(int id) {
		try (PreparedStatement ps = conn.prepareStatement(DELETE_COMMENT)) {
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean makeAdmin(String nickname) {
		try (PreparedStatement ps = conn.prepareStatement(MAKE_ADMIN)) {
			ps.setString(1, nickname);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Post> getPostsByComment(String request) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POSTS_BY_COMMENT)) {
			ps.setString(1, request);
			ResultSet rs = ps.executeQuery();
			List<Post> posts = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Post> getPostsByContent(String request) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POSTS_BY_CONTENT)) {
			ps.setString(1, request);
			ResultSet rs = ps.executeQuery();
			List<Post> posts = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Post> getPostsByTitle(String request) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POSTS_BY_TITLE)) {
			ps.setString(1, request);
			ResultSet rs = ps.executeQuery();
			List<Post> posts = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Post> getPostsByAuthor(String request) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POSTS_BY_AUTHOR)) {
			ps.setString(1, request);
			ResultSet rs = ps.executeQuery();
			List<Post> posts = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Post> getLastPosts(int count) {
		try (PreparedStatement ps = conn.prepareStatement(GET_LAST_POSTS)) {
			ps.setInt(1, count);
			ResultSet rs = ps.executeQuery();
			List<Post> posts = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean addComment(int postId, String content, String email) {
		try (PreparedStatement ps = conn.prepareStatement(ADD_COMMENT)) {
			ps.setInt(1, postId);
			ps.setString(2, content);
			ps.setString(3, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<Comment> getComments(int postId) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POST_COMMENTS)) {
			ps.setInt(1, postId);
			ResultSet rs = ps.executeQuery();
			List<Comment> comments = new ArrayList<>();
			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			while (rs.next()) {
				String author = rs.getString("nickname");
				String content = rs.getString("content");
				Timestamp stamp = rs.getTimestamp("commentTime");
				int id = rs.getInt("id");
				comments.add(new Comment(author, content, f.format(new Date(stamp.getTime())), id));
			}
			return comments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean addPost(String title, String content, String email) {
		try (PreparedStatement ps = conn.prepareStatement(ADD_NEW_POST)) {
			ps.setString(1, title);
			ps.setString(2, content);
			ps.setString(3, email);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Post> getPosts(String email) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POSTS)) {
			List<Post> posts = new ArrayList<>();
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				posts.add(new Post(id, title, content, postDate, author));
			}
			return posts;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Post getPost(int id) {
		try (PreparedStatement ps = conn.prepareStatement(GET_POST)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int postId = rs.getInt("id");
				String title = rs.getString("title");
				String content = rs.getString("content");
				Date postDate = rs.getDate("postDate");
				String author = rs.getString("nickname");
				Post post = new Post(postId, title, content, postDate, author);
				return post;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isEmailUnique(String email) {
		try (PreparedStatement ps = conn.prepareStatement(CHECK_EMAIL_UNIQUE)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isNicknameUnique(String nickname) {
		try (PreparedStatement ps = conn.prepareStatement(CHECK_NICKNAME_UNIQUE)) {
			ps.setString(1, nickname);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isUserBlocked(String email) {
		try (PreparedStatement ps = conn.prepareStatement(IS_USER_BLOCKED)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean canUserSignIn(String email, String password) {
		try (PreparedStatement ps = conn.prepareStatement(CAN_USER_SIGN_IN)) {
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 1) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public User signIn(String email, String password) {
		try (PreparedStatement ps = conn.prepareStatement(SIGN_IN)) {
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String mail = rs.getString("email");
				String nick = rs.getString("nickname");
				boolean isAdmin = rs.getBoolean("isAdmin");
				Date regDate = rs.getDate("regDate");
				String about = rs.getString("about");
				User user = new User(nick, mail, regDate, isAdmin, about);
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getNicknameByEmail(String email) {
		try (PreparedStatement ps = conn.prepareStatement(GET_NICKNAME_BY_EMAIL)) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void close() throws SQLException {
		DriverManager.deregisterDriver(mysql);
	}

}
