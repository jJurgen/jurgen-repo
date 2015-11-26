package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MakeAdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nickname = req.getParameter("adminNickname");
		User curUser = (User) req.getSession().getAttribute("currentUser");
		if ((curUser != null) && (curUser.isAdmin())) {
			if (!nickname.trim().equals("")) {
				final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
				dao.makeAdmin(nickname);
			}
		}
		resp.sendRedirect("protected/AdminPage.jsp");
	}

}
