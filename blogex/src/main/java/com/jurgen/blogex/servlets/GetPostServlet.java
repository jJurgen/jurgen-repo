package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class GetPostServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			int id = Integer.parseInt(req.getParameter("postId"));
			final BlogExDAO dao = (BlogExDAO) req.getServletContext().getAttribute(InitListener.DAO_ATTR);
			Post currentPost = dao.getPost(id);
			if (currentPost != null) {
				currentPost.setComments(dao.getComments(currentPost.getId()));
				req.getSession().setAttribute("currentPost", currentPost);
				RequestDispatcher rd = req.getRequestDispatcher("/BlogPage.jsp");
				rd.forward(req, resp);
				return;
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}	
		resp.sendRedirect("HomePage.jsp");
	}

}
