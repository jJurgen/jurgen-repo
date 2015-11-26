package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BlockUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nickname = req.getParameter("selectedUserNickname");
		User user = (User) req.getSession().getAttribute("currentUser");
		if ((user != null) && (user.isAdmin()) && (nickname != null)) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			if (dao.addUserToBlackList(nickname)) {
				String redirect = String.format("%s%s%s", getServletContext().getContextPath(),
						"/GetUserServlet?nickname=", nickname);
				resp.sendRedirect(redirect);
				return;
			}
		}
		resp.sendRedirect("HomePage.jsp");
	}

}
