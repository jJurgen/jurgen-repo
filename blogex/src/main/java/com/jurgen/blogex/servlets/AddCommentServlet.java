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

public class AddCommentServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		User curUser = (User) req.getSession().getAttribute("currentUser");
		if (curUser != null) {
			String content = req.getParameter("commentArea");
			try {
				int postId = Integer.parseInt(req.getParameter("postId"));
				if (!content.trim().equals("")) {
					final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
					if (dao.addComment(postId, content, curUser.getEmail())) {
						Post post = dao.getPost(postId);
						post.setComments(dao.getComments(post.getId()));
						req.getSession().setAttribute("currentPost", post);
					}
				}
				String redirect = String.format("%s%s%d", getServletContext().getContextPath(),
						"/GetPostServlet?postId=", postId);
				resp.sendRedirect(redirect);
				return;
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		resp.sendRedirect("HomePage.jsp");
	}

}
