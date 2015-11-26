package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RemovePostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("postId"));
		User curUser = (User) req.getSession().getAttribute("currentUser");
		if ((curUser != null)) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			if (dao.removePost(id)) {
				getServletContext().setAttribute(InitListener.LAST_POSTS_ATTR,
						dao.getLastPosts(InitListener.postsCount));
				if(!curUser.isAdmin()){
					String redirect = String.format("%s%s%s", getServletContext().getContextPath(), "/GetUserServlet?nickname=",
							curUser.getNickname());
					resp.sendRedirect(redirect);
					return;
				}
			}
		}
		resp.sendRedirect("HomePage.jsp");

	}

}
