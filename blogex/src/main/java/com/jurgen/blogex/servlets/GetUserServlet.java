package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GetUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String nickname = req.getParameter("nickname");
		RequestDispatcher rd = req.getRequestDispatcher("/UserInfoPage.jsp");		
		if(nickname != null){
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			User selectedUser = dao.getUserByNickname(nickname);
			if(selectedUser != null){
				List<Post> posts = dao.getPosts(selectedUser.getEmail());
				req.setAttribute("posts", posts);
				req.setAttribute("selectedUser", selectedUser);
				rd.forward(req, resp);
				return;
			}
		}
		req.setAttribute("errorMessage", String.format("%s%s", "Wrong nickname: ",nickname));
		rd.forward(req, resp);		
	}

}
