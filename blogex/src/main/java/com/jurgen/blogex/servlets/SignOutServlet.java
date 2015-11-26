package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignOutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().removeAttribute("currentUser");
		request.getSession().removeAttribute("posts");
		final BlogExDAO dao = (BlogExDAO) request.getServletContext().getAttribute(InitListener.DAO_ATTR);
		Cookie[] cookies = request.getCookies();		
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(cookie.getName().equals("rememberedUser")){
				dao.removeToken(cookie.getValue());
				cookie.setMaxAge(0);
				response.addCookie(cookie);				
			}
		}
		
		response.sendRedirect("HomePage.jsp");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	
}
