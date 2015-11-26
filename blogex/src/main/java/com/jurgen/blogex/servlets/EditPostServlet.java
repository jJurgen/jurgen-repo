package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String title = request.getParameter("titleArea");
		String content = request.getParameter("contentArea");
		User user = (User) request.getSession().getAttribute("currentUser");
		Post post = (Post) request.getSession().getAttribute("editingPost");
		if ((user != null) && (post != null) && (title != null) && (content != null)) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			if (dao.updatePost(title, content, post.getId())) {
				getServletContext().setAttribute(InitListener.LAST_POSTS_ATTR,
						dao.getLastPosts(InitListener.postsCount));
				List<Post> posts = dao.getPosts(user.getEmail());
				request.getSession().setAttribute("currentUser", user);
				request.getSession().setAttribute("posts", posts);
				String redirect = String.format("%s%s%s", getServletContext().getContextPath(), "/GetUserServlet?nickname=",
						user.getNickname());
				response.sendRedirect(redirect);
				return;
			}
		}
		request.getSession().removeAttribute("editingPost");
		response.sendRedirect("HomePage.jsp");
	}

}
