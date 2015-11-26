package com.jurgen.blogex.listeners;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener("InitListener")
public class InitListener implements ServletContextListener {
	public static final String DAO_ATTR = "dao";
	public static final String LAST_POSTS_ATTR = "lastPosts";
	public static final int postsCount = 10;
	
	public void contextInitialized(ServletContextEvent evt) {
		try {
			final BlogExDAO dao = new BlogExDAO();
			evt.getServletContext().setAttribute(DAO_ATTR, dao);
			List<Post> lastPosts = dao.getLastPosts(postsCount);
			evt.getServletContext().setAttribute(LAST_POSTS_ATTR, lastPosts);
		} catch (SQLException e) {
			throw new IllegalStateException("Can't initialize DAO");
		}
		
	}

	public void contextDestroyed(ServletContextEvent evt) {
		try {
			final BlogExDAO dao = (BlogExDAO) evt.getServletContext().getAttribute(DAO_ATTR);
			if (dao != null) {
				dao.close();
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Can't close DAO");
		}
	}

}
