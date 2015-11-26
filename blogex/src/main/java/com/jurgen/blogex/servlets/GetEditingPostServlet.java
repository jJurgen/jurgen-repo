package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetEditingPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int postId = Integer.parseInt(req.getParameter("postId"));
			User user = (User) req.getSession().getAttribute("currentUser");
			if (user != null) {
				final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
				Post post = dao.getPost(postId);
				if (post != null) {
					req.getSession().setAttribute("editingPost", post);
					resp.sendRedirect("UserResources/EditPostPage.jsp");
					return;
				}
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		resp.sendRedirect("HomePage.jsp");
	}

}
