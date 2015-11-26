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

public class RemoveCommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int commentId = Integer.parseInt(req.getParameter("commentId"));
		int postId = Integer.parseInt(req.getParameter("postId"));
		User curUser = (User) req.getSession().getAttribute("currentUser");
		if ((curUser != null) && (curUser.isAdmin())) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			if (dao.removeComment(commentId)) {
				Post post = dao.getPost(postId);
				post.setComments(dao.getComments(post.getId()));
				req.getSession().setAttribute("currentPost", post);
			}
		}
		String redirect = String.format("%s%s%d", getServletContext().getContextPath(), "/GetPostServlet?postId=",
				postId);
		resp.sendRedirect(redirect);
	}

}
