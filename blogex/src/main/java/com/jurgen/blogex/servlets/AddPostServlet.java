package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class AddPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title = req.getParameter("titleArea");
		String content = req.getParameter("contentArea");
		User user = (User) req.getSession().getAttribute("currentUser");
		if (user != null) {
			if ((!title.trim().equals("")) && (!content.trim().equals(""))) {
				final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
				String curUserEmail = user.getEmail();
				if (dao.addPost(title.trim(), content.trim(), curUserEmail)) {
					List<Post> posts = dao.getPosts(curUserEmail);
					req.getSession().setAttribute("posts", posts);
					getServletContext().setAttribute(InitListener.LAST_POSTS_ATTR,
							dao.getLastPosts(InitListener.postsCount));
				}
			}
		}
		resp.sendRedirect("UserResources/WritePostPage.jsp");
	}

}
